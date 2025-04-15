package com.example.AIGen.Controllers;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.AIGen.Dto.UserDetailsDTO;
import com.example.AIGen.models.User;
import com.example.AIGen.payload.response.ApiResponse;
import com.example.AIGen.services.BoondManagerAPIClient;
import com.example.AIGen.services.FileUploadService;
import com.example.AIGen.services.PowerPointService;
import com.example.AIGen.services.UserDetailsServiceImpl;
import com.fasterxml.jackson.databind.JsonNode;


@RestController
@RequestMapping("/boondmanager")
@CrossOrigin
public class BoondManagerController {

	@Autowired
	private BoondManagerAPIClient apiClient;

	@Autowired
	private PowerPointService powerPointService;
	
	@GetMapping("/opportunities")
	public JsonNode getOpportunities( @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "100") int limit) {
	    System.out.println("Page requested: " + page);
	    System.out.println("Limit requested: " + limit);
		return apiClient.getFromBoondManagerAPI("/opportunities", page, limit);
	}


    @GetMapping("/project")
    public JsonNode getAllProjects(@RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "100") int limit) {
    	return apiClient.getFromBoondManagerAPI("/projects", page, limit);
    }
    
    @GetMapping("/project/{id}")
    public JsonNode getProjectbyId(@PathVariable String id,@RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "100") int limit) {
    	return apiClient.getFromBoondManagerAPI("/projects/" + id, page, limit);
    }
    
    @GetMapping("/project/{id}/information")
    public JsonNode getProjectInformation(@PathVariable String id,@RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "100") int limit) {
    	return apiClient.getFromBoondManagerAPI("/projects/" + id + "/information", page, limit);
    }

    @GetMapping("/documents/{id}")
    public ResponseEntity<InputStreamResource> getDocumentbyId(@PathVariable String id){
        try {
            // Get the PDF input stream from the BoondManager API client
            InputStream documentStream = apiClient.getDocumentById(id);

            // Set headers for returning the PDF
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);

            // Stream the PDF back to the client
            InputStreamResource resource = new InputStreamResource(documentStream);

            return new ResponseEntity<>(resource, headers, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    
	@GetMapping("/opportunities/default")
	public JsonNode getOpportunitiesDefault(@RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "100") int limit) {
		return apiClient.getFromBoondManagerAPI("/opportunities/default",page, limit);
	}
	
    @GetMapping("/opportunities/{id}")
    public JsonNode getOpportunityById(@PathVariable String id) {
        return apiClient.getOppByIdFromBoondManagerAPI("/opportunities/" + id);
    }

    @GetMapping("/opportunities/{id}/rights")
    public JsonNode getRightsOpportunities(@PathVariable String id,@RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "100") int limit) {
    	return apiClient.getFromBoondManagerAPI("/opportunities/" + id + "/rights",page,limit);
    }

    @GetMapping("/opportunities/{id}/information")
    public JsonNode getOpportunityInformation(@PathVariable String id) {
        return apiClient.getOppByIdFromBoondManagerAPI("/opportunities/" + id + "/information");
    }

    @GetMapping("/opportunities/{id}/attached-flags")
    public JsonNode getOpportunityAttachedFlags(@PathVariable String id,@RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "100") int limit) {
        return apiClient.getFromBoondManagerAPI("/opportunities/" + id + "/attached-flags",page,limit);
    }
    
    @GetMapping("/opportunities/{id}/simulation")
    public JsonNode getOpportunitySimulation(@PathVariable String id,@RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "100") int limit) {
        return apiClient.getFromBoondManagerAPI("/opportunities/" + id + "/simulation",page,limit);
    }
    
//    @PutMapping("/opportunities/{id}/simulation")
//    public String getOpportunitySimulation(@PathVariable String id, @RequestBody String jsonBody) {
//        return apiClient.putToBoondManagerAPI("/opportunities/" + id + "/simulation", jsonBody);
//    }
    
    @GetMapping("/opportunities/{id}/positionings")
    public JsonNode getOpportunityPositionings(@PathVariable String id,@RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "100") int limit) {
        return apiClient.getFromBoondManagerAPI("/opportunities/" + id + "/positionings",page,limit);
    }
    
    @GetMapping("/opportunities/{id}/actions")
    public JsonNode getOpportunityActions(@PathVariable String id,@RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "100") int limit) {
        return apiClient.getFromBoondManagerAPI("/opportunities/" + id + "/actions",page,limit);
    }
    
    @GetMapping("/opportunities/{id}/projects")
    public JsonNode getOpportunityProjects(@PathVariable String id,@RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "100") int limit) {
        return apiClient.getFromBoondManagerAPI("/opportunities/" + id + "/projects",page,limit);
    }
    
    @GetMapping("/opportunities/{id}/download")
    public JsonNode getOpportunityDownload(@PathVariable String id,@RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "100") int limit) {
        return apiClient.getFromBoondManagerAPI("/opportunities/" + id + "/download",page,limit);
    }
    
    @GetMapping("/opportunities/{id}/tasks")
    public JsonNode getOpportunityTasks(@PathVariable String id,@RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "100") int limit) {
        return apiClient.getFromBoondManagerAPI("/opportunities/" + id + "/tasks",page,limit);
    }
    
    @GetMapping("/ressource")
    public JsonNode getRessource(@RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "100") int limit){
    	return apiClient.getFromBoondManagerAPI("/resources", page, limit);
    }
    
    @GetMapping("/ressource/{id}/")
    public ResponseEntity<JsonNode> getRessourceById(@RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "100") int limit, 
            @PathVariable String id) {

    	 JsonNode response = apiClient.getRessourceFromBoondManagerAPI("/resources/" + id, page, limit);

    	    // Check if "data" and "attributes" nodes are present
    	    if (response.has("data") && response.get("data").has("attributes")) {
    	        String level = response.get("data").get("attributes").get("level").asText();

    	        // Only return response if access level matches "resource"
    	        if ("resource".equalsIgnoreCase(level)) {
    	            return ResponseEntity.ok(response);
    	        } else {
    	            // Return forbidden status if the user doesn't have the right access level
    	            return ResponseEntity.status(403).body(null); // Optionally, add a custom error message
    	        }
    	    } else {
    	        // Return bad request status if the expected data is missing
    	        return ResponseEntity.badRequest().body(null); // Optionally, add a custom error message
    	    }
    	 
    }
    @GetMapping("/ressource/{id}/information")
    public JsonNode getRessourceByIdinfromation(@RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "100") int limit, 
            @PathVariable String id) {

    	return apiClient.getRessourceFromBoondManagerAPI("/resources/" + id + "/information", page, limit);

    	 
    }
    
    

///////////////////////// Api for Opportunities DropDown list ////////////////////
//    @GetMapping("/opportunities/dropdown")
//    public ResponseEntity<List<Map<String, Object>>> getOpportunitiesDropdown(
//            @RequestParam(defaultValue = "1") int page,
//            @RequestParam(defaultValue = "100") int limit) {
//        
//        List<Map<String, Object>> dropdownOptions = apiClient.getOpportunitiesDropdown(page, limit);
//        return ResponseEntity.ok(dropdownOptions);
//    }

    @GetMapping("/thumbnails/{id}")
    public ResponseEntity<byte[]> getThumbnail(@PathVariable String id) {
        try {
            byte[] imageData = apiClient.fetchThumbnail(id);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG); // Adjust based on image type
            return new ResponseEntity<>(imageData, headers, HttpStatus.OK);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("Error fetching thumbnail: " + e.getMessage()).getBytes());
        }
    }
    
    @GetMapping("/opportunities/details/dashboard/{id}")
    public ResponseEntity<Map<String, Object>> getOpportunityDetails(@PathVariable String id) {
        try {
            Map<String, Object> opportunityDetails = apiClient.getOpportunityDetailsById(id);
            if (opportunityDetails.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "Opportunity not found with ID: " + id));
            }
            return ResponseEntity.ok(opportunityDetails);
        } catch (Exception e) {
            // Log the error for debugging
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to fetch opportunity details"));
        }
    }

    @GetMapping("/opprtunities/with-documents")
    public ResponseEntity<Map<String, Object>> getOpportunitiesWithDocuments(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "100") int limit) {
        try {
            Map<String, Object> response = apiClient.getOpportunitiesWithDocuments(page, limit);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // Log the error for debugging
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("status", "FAILURE", "message", "An error occurred while fetching opportunities with documents"));
        }
    }  
}
