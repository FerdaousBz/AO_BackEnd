package com.example.AIGen.services;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.stereotype.Service;
import com.itextpdf.layout.element.Paragraph;

import com.example.AIGen.Repository.UploadFileRepository;
import com.example.AIGen.models.UploadFile;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;

import jakarta.activation.DataSource;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

	
	@Value("${spring.mail.username}")
	private String fromEmail;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UploadFileRepository uploadFileRepository;

    @Autowired
    private FileUploadService fileUploadService;
    
    @Autowired
    private BoondManagerAPIClient apiClient;


    public void sendResumeWithAttachment(String recipientEmail, String fileId, List<String> additionalResourceEmails) throws Exception {
        // Create the "To" addresses list
        List<InternetAddress> toAddresses = new ArrayList<>();

        // Validate and add recipientEmail if provided
        if (recipientEmail != null && !recipientEmail.isEmpty()) {
            if (!isValidEmail(recipientEmail)) {
                throw new IllegalArgumentException("Error: Invalid email domain. The email must belong to '@jems-group.com'.");
            }
            toAddresses.add(new InternetAddress(recipientEmail, "Primary Recipient"));
        }

        // Validate and add additional resource emails
        if (additionalResourceEmails != null && !additionalResourceEmails.isEmpty()) {
            for (String email : additionalResourceEmails) {
                if (!isValidEmail(email)) {
                    throw new IllegalArgumentException("Error: Invalid email domain. The email must belong to '@jems-group.com'.");
                }
                toAddresses.add(new InternetAddress(email, "Additional Recipient"));
            }
        }

        if (toAddresses.isEmpty()) {
            throw new IllegalArgumentException("Error: No valid recipients provided.");
        }

        // Fetch resume data using fileId
        ResponseEntity<Map<String, Object>> responseEntity = fileUploadService.getResumeByFileId(fileId);
        if (!responseEntity.getStatusCode().is2xxSuccessful() || responseEntity.getBody() == null) {
            throw new RuntimeException("Error fetching resume for fileId: " + fileId);
        }

        // Extract the resume data
        Map<String, Object> resumeData = responseEntity.getBody();
        List<Map<String, Object>> files = (List<Map<String, Object>>) resumeData.get("file");

        if (files == null || files.isEmpty()) {
            throw new RuntimeException("No files found for the provided file ID.");
        }

        Map<String, Object> firstFileSummary = (Map<String, Object>) files.get(0).get("summary");
        String title = firstFileSummary != null ? (String) firstFileSummary.get("title") : "Unknown File";

        // Generate PDF and email content
        byte[] pdfBytes = createPdfAttachment(files);
        ByteArrayResource resource = new ByteArrayResource(pdfBytes);
        String pdfFileName = title.replaceAll("[^a-zA-Z0-9\\.\\-]", "_") + ".pdf";
        String emailContent = createEmailContent(title);

        // Sending the email
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(toAddresses.toArray(new InternetAddress[0]));
            helper.setSubject("Résumé de fichier: " + title);
            helper.setText(emailContent);
            helper.addAttachment(pdfFileName, resource);

            mailSender.send(message);
            System.out.println("Email sent to: " + toAddresses);
        } catch (Exception e) {
            System.err.println("Error sending email: " + e.getMessage());
            throw new RuntimeException("Failed to send email.", e);
        }
    }
	    private byte[] createPdfAttachment(List<Map<String, Object>> files) throws Exception {
	        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
	            PdfWriter writer = new PdfWriter(byteArrayOutputStream);
	            PdfDocument pdfDocument = new PdfDocument(writer);
	            Document document = new Document(pdfDocument);
	
	            for (Map<String, Object> fileInfo : files) {
	                Map<String, Object> summary = (Map<String, Object>) fileInfo.get("summary");
	                document.add(new Paragraph("Nom du fichier: " + fileInfo.get("file_name")));
	                document.add(new Paragraph("\n"));
	                document.add(new Paragraph("Titre: " + summary.get("title")));
	                document.add(new Paragraph("\n"));
	                document.add(new Paragraph("Description: " + summary.get("description")));
	                document.add(new Paragraph("\n"));
	                document.add(new Paragraph("Date de génération: " + summary.get("date_gen_résumé")));
	            }
	
	            document.close();
	            return byteArrayOutputStream.toByteArray();
	        }
	    }

    private String createEmailContent(String title) {
        return "Bonjour Madame/Monsieur,\n\n" +
                "Vous trouvez en pièce jointe le  \"" + title + "\"\n\n" +
                "Bien à vous,\n" +
                "Équipe Jems";
    }
    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@jems-group\\.com$";
        return email != null && email.matches(emailRegex);
    }

    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
//    // Method to send the resume email with attachment
//    public void sendResumeWithAttachment(String to, String fileId) throws Exception {
//    	// Check if the email is empty or does not contain "@jems-group"
//        if (to == null || to.trim().isEmpty()) {
//            throw new IllegalArgumentException("Error: Email cannot be empty.");
//        }
//        if (!to.contains("@jems-group")) {
//            throw new IllegalArgumentException("Error: Invalid email domain. The email must belong to '@jems-group'.");
//        }
//
//        // Call BoondManager API to retrieve resources
//        JsonNode resourcesResponse = apiClient.getFromBoondManagerAPI("/resources", 1, 100);
//        boolean emailExists = false;
//
//        // Find resource ID by email and extract first and last name
//        if (resourcesResponse.has("data")) {
//            for (JsonNode resource : resourcesResponse.get("data")) {
//                String resourceEmail = resource.get("attributes").get("email1").asText();
//                if (to.equals(resourceEmail)) {
//                    emailExists = true; // Email found in BoondManager
//                    break;
//                }
//            }
//        }
//
//        
//        if (!emailExists) {
//            throw new IllegalArgumentException("Error: The provided email does not exist in BoondManager.");
//        }
//
//        // Fetch resume data using fileId
//        ResponseEntity<Map<String, Object>> responseEntity = fileUploadService.getResumeByFileId(fileId);
//        if (!responseEntity.getStatusCode().is2xxSuccessful()) {
//            throw new RuntimeException("Error fetching resume: " + responseEntity.getBody());
//        }
//
//        // Extract the resume data
//        Map<String, Object> resumeData = responseEntity.getBody();
//        if (resumeData == null || !resumeData.containsKey("file")) {
//            throw new RuntimeException("No resume data found for the provided file ID.");
//        }
//
//        List<Map<String, Object>> files = (List<Map<String, Object>>) resumeData.get("file");
//
//        // Extract the title correctly
//        String title = (String) ((Map<String, Object>) ((Map<String, Object>) files.get(0)).get("summary")).get("title");
//        String subject = "Résumé de fichier: " + title;
//
//        // Create the email content
//        String emailContent = createEmailContent(title);
//
//        // Create the email message with attachment
//        try {
//            MimeMessage message = mailSender.createMimeMessage();
//            MimeMessageHelper helper = new MimeMessageHelper(message, true); // true for multipart
//            helper.setTo(to);
//            helper.setSubject(subject);
//            helper.setText(emailContent);
//
//            // Create a PDF attachment
//            byte[] pdfBytes = createPdfAttachment(files);
//            ByteArrayResource resource = new ByteArrayResource(pdfBytes);
//
//            // Set the PDF filename as the extracted title
//            String pdfFileName = title.replaceAll("[^a-zA-Z0-9\\.\\-] é", "_") + ".pdf"; // Sanitize the title for filename
//            helper.addAttachment(pdfFileName, resource);
//
//            // Send the email
//            mailSender.send(message);
//        } catch (MailException e) {
//            throw new RuntimeException("Error sending email: " + e.getMessage(), e);
//        } catch (Exception e) {
//            throw new RuntimeException("Error creating or sending email: " + e.getMessage(), e);
//        }
//    }
//
//    // Method to create PDF attachment
//    private byte[] createPdfAttachment(List<Map<String, Object>> files) throws Exception {
//        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
//            PdfWriter writer = new PdfWriter(byteArrayOutputStream);
//            PdfDocument pdfDocument = new PdfDocument(writer);
//            Document document = new Document(pdfDocument);
//
//            for (Map<String, Object> fileInfo : files) {
//                Map<String, Object> summary = (Map<String, Object>) fileInfo.get("summary");
//                document.add(new Paragraph("Nom du fichier: " + fileInfo.get("file_name")));
//                document.add(new Paragraph("\n")); // Add a new line
//                document.add(new Paragraph("Titre: " + summary.get("title")));
//                document.add(new Paragraph("\n")); // Add a new line
//                document.add(new Paragraph("Description: " + summary.get("description")));
//                document.add(new Paragraph("\n")); // Add a new line
////                document.add(new Paragraph("ID du résumé: " + summary.get("summary_id")));
//                document.add(new Paragraph("Date de génération: " + summary.get("date_gen_résumé")));
//            }
//
//            document.close();
//            return byteArrayOutputStream.toByteArray();
//        }
//    }
////
////    // Method to create email content
//    private String createEmailContent(String title) {
//        StringBuilder content = new StringBuilder();
//        content.append("Bonjour Madame/Monsieur,\n\n");
//        content.append("Vous trouvez en pièce jointe le résumé de \"").append(title).append("\"\n\n");
//        content.append("Bien à vous,\n");
//        content.append("Équipe Jems");
//        return content.toString();
//    }
}