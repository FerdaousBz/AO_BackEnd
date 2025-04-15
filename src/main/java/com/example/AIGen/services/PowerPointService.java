package com.example.AIGen.services;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFTextBox;
import org.hibernate.exception.DataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.AIGen.Exception.PptGenerationException;
import com.example.AIGen.Repository.UploadFileRepository;
import com.example.AIGen.models.UploadFile;
import com.example.AIGen.payload.response.ApiResponse;
import com.example.AIGen.payload.response.PptGenratedResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class PowerPointService {
	
//	@Value("${file.generate_ppt.url}")
	private String generatePptUrl;
//	@Value("${file.get-ppt.url}")
	private String getPptUrl;
	
	private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper; 
    
    @Autowired 
    private FileUploadService fileUploadService;
    
    
    
    @Autowired
    public PowerPointService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }
    
 public ApiResponse generatePptFromApi(String fileId) {
	 try {
	        // Step 1: Retrieve resume data to get summary_id
	        ResponseEntity<Map<String, Object>> resumeResponse = fileUploadService.getResumeByFileId(fileId);
	        if (resumeResponse.getStatusCode() != HttpStatus.OK) {
	            return new ApiResponse("FAILED", "Failed to retrieve resume. Status: " + resumeResponse.getStatusCode(), null);
	        }

	        // Step 2: Extract summary_id from the nested structure
	        Map<String, Object> resumeData = resumeResponse.getBody();
	        if (resumeData == null || !resumeData.containsKey("file")) {
	            return new ApiResponse("FAILED", "Resume data is missing or invalid", null);
	        }

	        List<Map<String, Object>> fileList = (List<Map<String, Object>>) resumeData.get("file");
	        if (fileList == null || fileList.isEmpty()) {
	            return new ApiResponse("FAILED", "File list is empty in resume data", null);
	        }

	        Map<String, Object> fileData = fileList.get(0); // Assuming the first file is relevant
	        if (fileData == null || !fileData.containsKey("summary")) {
	            return new ApiResponse("FAILED", "Summary data is missing in file object", null);
	        }

	        // Corrected path for "summary" key
	        Map<String, Object> summaryData = (Map<String, Object>) fileData.get("summary");
	        if (summaryData == null || !summaryData.containsKey("summary_id")) {
	            return new ApiResponse("FAILED", "Summary data or summary_id is missing", null);
	        }

	        String summaryId = (String) summaryData.get("summary_id");
	        if (summaryId == null || summaryId.isEmpty()) {
	            return new ApiResponse("FAILED", "Summary ID is empty", null);
	        }

	        // Step 3: Prepare the URL with summary_id for generating the PPT
	        String apiUrlWithId = generatePptUrl.replace("{summary_id}", summaryId);

	        // Step 4: Send POST request to external API to generate PPT
	        ResponseEntity<JsonNode> response = restTemplate.exchange(
	            apiUrlWithId,
	            HttpMethod.POST,
	            HttpEntity.EMPTY,
	            JsonNode.class
	        );

	        if (response.getStatusCode() == HttpStatus.CONFLICT) {
	            JsonNode responseBody = response.getBody();
	            String message = responseBody != null ? responseBody.path("message").asText("Conflict occurred") : "Conflict occurred";
	            String pptId = responseBody != null ? responseBody.path("ppt_id").asText(null) : null;
	            return new ApiResponse("FAILED", message, pptId);
	        }

	        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
	            return new ApiResponse("FAILED", "Failed to generate PPT. Status: " + response.getStatusCode(), null);
	        }

	        // Step 5: Extract and map response data
	        JsonNode responseBody = response.getBody();
	        if (responseBody != null) {
	            // Assuming the API response has the ppt_id directly in the root or a different path
	            String pptId = responseBody.path("ppt_id").asText();
	            String pptName = responseBody.path("ppt_name").asText();
	            int version = responseBody.path("version").asInt();

	            Map<String, Object> pptResponse = new HashMap<>();
	            pptResponse.put("ppt_id", pptId);
	            pptResponse.put("ppt_name", pptName);
	            pptResponse.put("version", version);
	            pptResponse.put("summary_id", summaryId); // Returning the same summary_id

	            return new ApiResponse("SUCCESS", pptResponse, "PPT generated successfully");
	        }

	        return new ApiResponse("FAILED", "API response body is null", null);

	    } catch (Exception e) {
	        return new ApiResponse("FAILED", "Failed to call external API: " + e.getMessage(), null);
	    }
}
    public ApiResponse getPptFromApi(String summary_id) {
    	
        if (summary_id == null || summary_id.isEmpty()) {
            return new ApiResponse("FAILED", "Summary ID is missing", null);
        }

        String pptGeneratedUrl = getPptUrl.replace("{summary_id}", summary_id);
        ResponseEntity<JsonNode> response;

        try {
            // Send GET request to external API
            response = restTemplate.exchange(
                pptGeneratedUrl,
                HttpMethod.GET,
                HttpEntity.EMPTY,
                JsonNode.class
            );

            // Verify response status and required fields
            if (response.getStatusCode() == HttpStatus.OK && response.getBody().has("ppt_id")) {
                JsonNode responseBody = response.getBody();

                // Extract details from response body
                String pptContent = responseBody.path("ppt_content").asText("");
                String pptId = responseBody.path("ppt_id").asText();
                String pptUploadDate = responseBody.path("ppt_upload_date").asText("");
                String pptName = responseBody.path("pptname").asText("");
                int version = responseBody.path("version").asInt(0);

                PptGenratedResponse pptData = new PptGenratedResponse(
                    "200",
                    "PPT is ready",
                    pptContent,
                    pptId,
                    pptUploadDate,
                    pptName,
                    version
                );

                return new ApiResponse("SUCCESS", pptData, "PPT generated successfully");
            } else {
                return new ApiResponse("FAILED", "PPT generation failed or ppt_id is missing", null);
            }

        } catch (Exception e) {
            return new ApiResponse("FAILED", "Failed to call external API: " + e.getMessage(), null);
        }
    }

//  public PptGenratedResponse generatePptFromApi(String summary_id) {
//  	 String apiUrlWithId = generatePptUrl.replace("{summary_id}", summary_id);
//  	    ResponseEntity<JsonNode> response;
//
//  	    try {
//  	        response = restTemplate.exchange(
//  	            apiUrlWithId,
//  	            HttpMethod.POST,
//  	            HttpEntity.EMPTY,
//  	            JsonNode.class
//  	        );
//  	    } catch (Exception e) {
//  	        throw new RuntimeException("Failed to call external API: " + e.getMessage(), e);
//  	    }
//
//  	    // Check for 409 Conflict status
//  	    if (response.getStatusCode() == HttpStatus.CONFLICT) {
//  	        JsonNode body = response.getBody();
//  	        String message = body != null ? body.path("message").asText() : "Conflict occurred";
//  	        String pptId = body != null ? body.path("ppt_id").asText() : null; // May still be null if not present
//  	        throw new PptGenerationException(message, pptId); // Throwing exception with the id if exists
//  	    }
//
//  	    // Check for other non-successful responses
//  	    if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
//  	        throw new RuntimeException("Failed to fetch data from external API. Status: " + response.getStatusCode());
//  	    }
//
//  	    // Return successful response
//  	    String pptId = response.getBody().path("ppt_id").asText(); // Get ppt_id from successful response
//  	    return new PptGenratedResponse("200", "PPT generated successfully", pptId);
//  	}
}
