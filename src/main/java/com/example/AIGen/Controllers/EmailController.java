package com.example.AIGen.Controllers;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.AIGen.services.EmailService;
import com.example.AIGen.services.FileUploadService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/api/emails")
@CrossOrigin
public class EmailController {

	@Autowired
	private EmailService emailService;
	
	@Autowired
	private FileUploadService fileUploadService;
	
	  	@PostMapping("/send-resume")
	  	public ResponseEntity<String> sendResumeWithAttachment(
	  	        @RequestParam(required = false) String email,
	  	        @RequestParam String fileId,
	  	        @RequestParam(required = false) List<String> additionalResourceEmails) {

	  	    try {
	  	        if (fileId == null || fileId.trim().isEmpty()) {
	  	            return ResponseEntity.badRequest().body("Error: File ID is required.");
	  	        }

	  	        emailService.sendResumeWithAttachment(email, fileId, additionalResourceEmails);
	  	        return ResponseEntity.ok("Email sent successfully.");
	  	    } catch (IllegalArgumentException e) {
	  	        return ResponseEntity.badRequest().body("Error: " + e.getMessage());
	  	    } catch (Exception e) {
	  	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send email: " + e.getMessage());
	  	    }
	    }
	
	
	
	
//    @PostMapping("/send-resume")
//    public ResponseEntity<String> sendResumeEmail(@RequestParam String email, @RequestParam String fileId) {
//        try {
//            emailService.sendResumeWithAttachment(email, fileId);
//            return ResponseEntity.ok("Email sent successfully!");
//        } catch (IllegalArgumentException e) {
//            return ResponseEntity.badRequest().body(e.getMessage()); // 400 Bad Request for invalid email input
//        } catch (RuntimeException e) {
//            return ResponseEntity.status(500).body("Error: " + e.getMessage()); // 500 Internal Server Error for other issues
//        } catch (Exception e) {
//            return ResponseEntity.status(500).body("Unexpected error: " + e.getMessage()); // Catch all other errors
//        }
//    }
}
