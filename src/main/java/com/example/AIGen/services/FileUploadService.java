package com.example.AIGen.services;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.AIGen.Repository.UploadFileRepository;
//import com.example.AIGen.mappers.UploadFileMapper;
import com.example.AIGen.models.Project;
import com.example.AIGen.models.UploadFile;
import com.example.AIGen.payload.response.ApiResponse;
import com.example.AIGen.payload.response.PptGenratedResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class FileUploadService {
	
   private final WebClient webClient;
    
//	@Value("${file.upload.url}")
	private String uploadUrl;

//    @Value("${file.generate-summary.url}")
    private String generateSummaryUrl;

//    @Value("${file.get-summary.url}")
    private String getResume;
    
    private final RestTemplate restTemplate;
    
    @Autowired
    private  UploadFileRepository uploadFileRepository; 
    
    @Autowired
    private EmailService emailService;
    
    @Autowired
    private BoondManagerAPIClient boondManagerAPIClient;
    
    @Autowired
    private PowerPointService powerPointService;
    
    @Autowired 
    private DictionaryService dictionaryService;
    
    @Autowired
    public FileUploadService(RestTemplate restTemplate, WebClient webClient, UploadFileRepository uploadFileRepository) {
		this.webClient = webClient;
		this.restTemplate = restTemplate;
        this.uploadFileRepository = uploadFileRepository;
    }

    //Upload file to generate resume for CDC
    public ResponseEntity<?> uploadFile(File file) {
    	FileSystemResource resource = new FileSystemResource(file);
        String response;

        if (!file.exists()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("File does not exist.");
        }

        try {
            response = webClient.post()
                    .uri(uploadUrl)
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .body(BodyInserters.fromMultipartData("file", resource))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("File upload failed: " + e.getMessage());
        }

        System.out.println("API Response: " + response);
        return ResponseEntity.ok(response);
        

    }
//        try {
//            // Fetch opportunity details from BoondManager API
//            JsonNode opportunityData = boondManagerAPIClient.getOppByIdFromBoondManagerAPI("/opportunities/" + opportunityId + "/information");
//            
//            // Extract opportunity attributes
//            JsonNode attributes = opportunityData.at("/data/attributes");
//            Map<String, Object> opportunityDetails = new HashMap<>();
//            opportunityDetails.put("id", opportunityData.at("/data/id").asText());
//            opportunityDetails.put("type", opportunityData.at("/data/type").asText());
//            opportunityDetails.put("creationDate", attributes.path("creationDate").asText());
//            opportunityDetails.put("updateDate", attributes.path("updateDate").asText());
//            opportunityDetails.put("title", attributes.path("title").asText());
//            opportunityDetails.put("reference", attributes.path("reference").asText());
//            opportunityDetails.put("startDate", attributes.path("startDate").asText());
//            opportunityDetails.put("expertiseArea", attributes.path("expertiseArea").asText());
//            opportunityDetails.put("activityAreas", attributes.path("activityAreas"));
//            opportunityDetails.put("closingDate", attributes.path("closingDate").asText());
//
//            // Extract included resources
//            List<Map<String, Object>> includedResources = new ArrayList<>();
//            for (JsonNode includedNode : opportunityData.withArray("included")) {
//                Map<String, Object> resourceDetails = new HashMap<>();
//                resourceDetails.put("id", includedNode.path("id").asText());
//                resourceDetails.put("type", includedNode.path("type").asText());
//                resourceDetails.put("attributes", includedNode.path("attributes"));
//                includedResources.add(resourceDetails);
//            }
//
//            // Create response with opportunity details
//            Map<String, Object> combinedResponse = new HashMap<>();
//            combinedResponse.put("opportunityDetails", opportunityDetails);
//            combinedResponse.put("includedResources", includedResources);
//
//            // Retrieve the file ID associated with the opportunity
//            JsonNode filesNode = opportunityData.at("/data/relationships/files/data");
//            String fileId = filesNode.isArray() && filesNode.size() > 0 ? filesNode.get(0).get("id").asText() : null;
//
//            if (fileId != null) {
//                // Attempt to retrieve an existing resume by file ID
//                try {
//                    ResponseEntity<Map<String, Object>> existingResumeResponse = getResumeByFileId(fileId);
//                    if (existingResumeResponse.getStatusCode() == HttpStatus.OK) {
//                        Map<String, Object> resumeData = existingResumeResponse.getBody();
//                        combinedResponse.put("resumeData", resumeData != null ? resumeData.get("file") : "No resume data found.");
//                        return ResponseEntity.ok(combinedResponse);
//                    }
//                } catch (Exception e) {
//                    // If resume retrieval fails, log and continue to try summary generation
//                    combinedResponse.put("resumeDataError", "Failed to retrieve existing resume: " + e.getMessage());
//                }
//
//                // If no resume exists, attempt to generate a new summary
//                try {
//                    ResponseEntity<Map<String, Object>> generatedSummaryResponse = generateSummary(fileId);
//                    if (generatedSummaryResponse.getStatusCode() == HttpStatus.OK) {
//                        combinedResponse.put("resumeData", generatedSummaryResponse.getBody().get("summary"));
//                    } else {
//                        combinedResponse.put("resumeDataError", "Failed to generate summary for file ID: " + fileId);
//                    }
//                } catch (Exception e) {
//                    combinedResponse.put("resumeDataError", "Error generating summary: " + e.getMessage());
//                }
//            } else {
//                // If no file ID is found, add message indicating so
//                combinedResponse.put("resumeData", "No associated file found for this opportunity.");
//            }
//
//            // Return the combined response, with summary errors included if necessary
//            return ResponseEntity.ok(combinedResponse);
//
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(Collections.singletonMap("error", "Error processing opportunity and generating summary: " + e.getMessage()));
//        }
//    }
    //Do not touche this api 
    public ResponseEntity<?> generateSummaryForOpportunity(String opportunityId) {
        try {
            // Fetch opportunity details from BoondManager API
            JsonNode opportunityData = boondManagerAPIClient.getOppByIdFromBoondManagerAPI("/opportunities/" + opportunityId + "/information");

            // Extract opportunity attributes
            JsonNode attributes = opportunityData.at("/data/attributes");
            Map<String, Object> opportunityDetails = new HashMap<>();
            opportunityDetails.put("id", opportunityData.at("/data/id").asText());
            opportunityDetails.put("type", opportunityData.at("/data/type").asText());
            opportunityDetails.put("creationDate", attributes.path("creationDate").asText());
            opportunityDetails.put("updateDate", attributes.path("updateDate").asText());
            opportunityDetails.put("title", attributes.path("title").asText());
            opportunityDetails.put("reference", attributes.path("reference").asText());
            opportunityDetails.put("startDate", attributes.path("startDate").asText());
            opportunityDetails.put("expertiseArea", attributes.path("expertiseArea").asText());
            opportunityDetails.put("activityAreas", attributes.path("activityAreas"));
            opportunityDetails.put("closingDate", attributes.path("closingDate").asText());

            // Extract included resources (if any)
            List<Map<String, Object>> includedResources = new ArrayList<>();
            for (JsonNode includedNode : opportunityData.withArray("included")) {
                Map<String, Object> resourceDetails = new HashMap<>();
                resourceDetails.put("id", includedNode.path("id").asText());
                resourceDetails.put("type", includedNode.path("type").asText());
                resourceDetails.put("attributes", includedNode.path("attributes"));
                includedResources.add(resourceDetails);
            }

            // Combine opportunity details and resources into a response
            Map<String, Object> combinedResponse = new HashMap<>();
            combinedResponse.put("opportunityDetails", opportunityDetails);
            combinedResponse.put("includedResources", includedResources);

            // Retrieve the file ID associated with the opportunity
            JsonNode filesNode = opportunityData.at("/data/relationships/files/data");
            String fileId = filesNode.isArray() && filesNode.size() > 0 ? filesNode.get(0).get("id").asText() : null;

            if (fileId != null) {
                // Step 1: Check if a resume already exists for the file ID
                ResponseEntity<Map<String, Object>> existingResumeResponse = getResumeByFileId(fileId);

                if (existingResumeResponse.getStatusCode() == HttpStatus.OK) {
                    // Resume already exists, include it in the response
                    Map<String, Object> resumeData = existingResumeResponse.getBody();
                    combinedResponse.put("resumeData", resumeData != null ? resumeData.get("file") : "No resume data found.");
                    return ResponseEntity.ok(combinedResponse);
                }

                // Step 2: If no resume exists, attempt to generate a new summary
                ResponseEntity<Map<String, Object>> generatedSummaryResponse = generateSummary(fileId);

                if (generatedSummaryResponse.getStatusCode() == HttpStatus.OK) {
                    // Successfully generated summary, include it in the response
                    combinedResponse.put("resumeData", generatedSummaryResponse.getBody().get("summary"));
                    return ResponseEntity.ok(combinedResponse);
                } else {
                    // If summary generation fails, include the error message and BoondManager opportunity data
                    combinedResponse.put("error", "Failed to generate summary for file ID: " + fileId);
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(combinedResponse);  // Returning both opportunity details and error message
                }
            } else {
                // If no file ID is found, return message indicating no file associated
                combinedResponse.put("resumeData", "No associated file found for this opportunity.");
                return ResponseEntity.ok(combinedResponse);
            }
        } catch (Exception e) {
            // Log the error and return a generic error message
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Error processing opportunity and generating summary: " + e.getMessage()));
        }
    }

//  public List<Map<String, Object>> getOpportunitiesDropdown(int page, int limit) {
//    List<Map<String, Object>> dropdownOptions = new ArrayList<>();
//
//    // Step 1: Fetch paginated list of opportunities
//    JsonNode allOpportunities = boondManagerAPIClient.getFromBoondManagerAPI("/opportunities", page, limit);
//    JsonNode opportunitiesData = allOpportunities.path("data");
//
//    if (opportunitiesData.isArray()) {
//        for (JsonNode opportunityNode : opportunitiesData) {
//            String opportunityId = opportunityNode.path("id").asText();
//
//            // Step 2: Fetch detailed information for each opportunity
//            JsonNode opportunityInfo = boondManagerAPIClient.getOppByIdFromBoondManagerAPI("/opportunities/" + opportunityId + "/information");
//
//            // Extract opportunity attributes
//            JsonNode attributes = opportunityInfo.at("/data/attributes");
//            Map<String, Object> opportunityDetails = new HashMap<>();
//            opportunityDetails.put("id", opportunityInfo.at("/data/id").asText());
//            opportunityDetails.put("type", opportunityInfo.at("/data/type").asText());
//            opportunityDetails.put("creationDate", attributes.path("creationDate").asText());
//            opportunityDetails.put("updateDate", attributes.path("updateDate").asText());
//            opportunityDetails.put("title", attributes.path("title").asText());
//            opportunityDetails.put("reference", attributes.path("reference").asText());
//            opportunityDetails.put("startDate", attributes.path("startDate").asText());
//            opportunityDetails.put("expertiseArea", attributes.path("expertiseArea").asText());
//            opportunityDetails.put("activityAreas", attributes.path("activityAreas"));
//            opportunityDetails.put("closingDate", attributes.path("closingDate").asText());
//
//            // Extract included resources (if any)
//            List<Map<String, Object>> includedResources = new ArrayList<>();
//            for (JsonNode includedNode : opportunityInfo.withArray("included")) {
//                Map<String, Object> resourceDetails = new HashMap<>();
//                resourceDetails.put("id", includedNode.path("id").asText());
//                resourceDetails.put("type", includedNode.path("type").asText());
//                resourceDetails.put("attributes", includedNode.path("attributes"));
//                includedResources.add(resourceDetails);
//            }
//
//            // Check for document presence
//            boolean hasDocument = false;
//            JsonNode filesNode = opportunityInfo.path("data").path("relationships").path("files").path("data");
//            hasDocument = filesNode.isArray() && filesNode.size() > 0;
//
//            // Check if resume data or PPT is generated
//            boolean hasGeneratedResume = false;
//            boolean hasGeneratedPPT = false;
//            JsonNode resumeNode = opportunityInfo.path("resumeData");
//            if (resumeNode.isArray() && resumeNode.size() > 0) {
//                JsonNode firstResume = resumeNode.get(0);
//                hasGeneratedResume = firstResume.has("summary") && firstResume.path("summary").has("summary_id");
//
//                // PPT Generation check based on summary_id
//                String summaryId = hasGeneratedResume ? firstResume.path("summary").path("summary_id").asText() : null;
//                if (summaryId != null) {
//                    ApiResponse pptStatus = powerPointService.getPptFromApi(summaryId);
//                    hasGeneratedPPT = "SUCCESS".equals(pptStatus.getStatus());
//                }
//            }
//
//            // Step 3: Include only opportunities with a document but no resume or PPT generated
//            if ((hasDocument && !hasGeneratedResume && !hasGeneratedPPT) || !hasDocument) {
//                // Combine opportunity details and resources into a single response structure
//                Map<String, Object> combinedResponse = new HashMap<>();
//                combinedResponse.put("opportunityDetails", opportunityDetails);
//                combinedResponse.put("includedResources", includedResources);
//                combinedResponse.put("hasDocument", hasDocument);
//
//                dropdownOptions.add(combinedResponse);
//            }
//        }
//    }
//
//    return dropdownOptions;
//}

    // Generate resume by id 
    public ResponseEntity<Map<String, Object>> generateSummary(String fileId) {
    	
    	 HttpHeaders headers = new HttpHeaders();
    	    headers.setContentType(MediaType.APPLICATION_JSON);

    	    Map<String, String> requestBody = new HashMap<>();
    	    requestBody.put("fileId", fileId);

    	    HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

    	    try {
    	        Optional<UploadFile> existingFile = uploadFileRepository.findByFileId(fileId);
    	        if (existingFile.isPresent()) {
    	            return ResponseEntity.ok(Collections.singletonMap("message", "Summary for fileId " + fileId + " already exists."));
    	        }

    	        ResponseEntity<String> response = restTemplate.postForEntity(generateSummaryUrl, requestEntity, String.class);
    	        if (response.getStatusCode().is2xxSuccessful()) {
    	            String responseBody = response.getBody();

    	            // Clean the response by parsing the string to JSON
    	            ObjectMapper objectMapper = new ObjectMapper();
    	            JsonNode jsonNode = objectMapper.readTree(responseBody);  // Parse the string into a JSON tree

    	            // Now return the cleaned response in a Map
    	            Map<String, Object> jsonResponse = new HashMap<>();
    	            jsonResponse.put("summary", jsonNode);  // Store the cleaned JSON node

    	            return ResponseEntity.ok(jsonResponse);
    	        } else {
    	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
    	                    .body(Collections.singletonMap("error", "Failed to generate summary. Status: " + response.getStatusCode()));
    	        }
    	    } catch (Exception e) {
    	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
    	                .body(Collections.singletonMap("error", "Error while generating summary: " + e.getMessage()));
    	    }
    }

    
    


    // Prepare the final response by combining opportunity data and summary
    private ResponseEntity<Map<String, Object>> prepareCombinedResponse(JsonNode opportunityData, Object summaryData) {
        Map<String, Object> response = new HashMap<>();
        response.put("opportunityData", opportunityData);
        response.put("summaryData", summaryData);
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<Map<String, Object>> getResumeByFileId(String fileId) {
        try {
            String url = getResume.replace("{file_id}", fileId);
            System.out.println("Fetching resume from URL: " + url);
            
            String response = webClient.get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            
            System.out.println("Raw API Response: " + response);
            
 
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> jsonResponse = objectMapper.readValue(response, Map.class);
     
            return ResponseEntity.ok(jsonResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Error retrieving resume: " + e.getMessage()));
        }
    } 

   


}
