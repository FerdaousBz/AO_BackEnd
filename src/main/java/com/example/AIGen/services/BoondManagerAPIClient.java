package com.example.AIGen.services;

import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.AIGen.models.User;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class BoondManagerAPIClient {

	@Value("${boondmanager.api.base-url}")
	private String baseUrl;

	@Value("${boondmanager.ui.username}")
	private String username;
	
	@Value("${boondmanager.ui.password}")
	private String password;
	
	@Autowired
	private DictionaryService dictionaryService;
	
    @Autowired
    private AsyncTaskExecutor taskExecutor; // You can use a configured AsyncTaskExecutor

	@Autowired
	private UserDetailsServiceImpl userService;
	
    private static final Map<String, JsonNode> dictionaryCache = new HashMap<>();
	
	private final HttpClient client = HttpClient.newHttpClient();
	private final ObjectMapper objectMapper = new ObjectMapper();
	  
	private String getAuthHeader() {
		String auth = username + ":" + password;
		return "Basic " + Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
	}
	
	//Get From boondmanger by id 
	public JsonNode getOppByIdFromBoondManagerAPI(String endpoint) {
	    HttpRequest request = HttpRequest.newBuilder()
	            .uri(URI.create(baseUrl + endpoint))
	            .header("Authorization", getAuthHeader())
	            .GET().build();
	      try {
	            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
	            if (response.statusCode() == 200) {
	                // Parse the response body as JsonNode
	                return objectMapper.readTree(response.body());
	            } else {
	                throw new RuntimeException("Error fetching data from BoondManager. Status: " + response.statusCode());
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	            throw new RuntimeException("Error while making API request: " + e.getMessage());
	        }
	}
	
	public ObjectMapper getObjectMapper() {
	        return objectMapper;
	    }
	// Get list of data from boondmanger
	public JsonNode getFromBoondManagerAPI(String endpoint, int page, int limit) {
        String paginatedEndpoint = String.format("%s?page=%d&maxResults=%d",baseUrl + endpoint, page, limit);
        
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(paginatedEndpoint))
                .header("Authorization", getAuthHeader())
                .GET().build();
	

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                // Parse the response body as JsonNode
                return objectMapper.readTree(response.body());
            } else {
                throw new RuntimeException("Error fetching data from BoondManager. Status: " + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error while making API request: " + e.getMessage());
        }
	}

	//Get ressource from boondmanager
	public JsonNode getRessourceFromBoondManagerAPI(String endpoint, int page, int limit) {
        String paginatedEndpoint = String.format("%s?page=%d&maxResults=%d",baseUrl + endpoint, page, limit);
        
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(paginatedEndpoint))
                .header("Authorization", getAuthHeader())
                .GET().build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                return objectMapper.readTree(response.body());
            } else {
                throw new RuntimeException("Error fetching data from BoondManager. Status: " + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error while making API request: " + e.getMessage());
        }
    }
	
	//fetch user details by id :
	public JsonNode getResourceDetailsById(String boondManagerId) {
		        // Fetch resource details from BoondManager API
		        JsonNode response = getRessourceFromBoondManagerAPI("/resources/" + boondManagerId, 1, 100);

		        // Validate the response and ensure "resource" level access
		        if (response.has("data") && response.get("data").has("attributes")) {
		            String level = response.get("data").get("attributes").get("level").asText();

		            // Return only if access level matches "resource"
		            if ("resource".equalsIgnoreCase(level)) {
		                return response;
		            }
		        }
		        return null; // Return null if response is invalid or access level does not match
		    }	


	//Get document pdf from boond
	public InputStream getDocumentById(String docId) throws Exception{
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/documents/" + docId))
                .header("Authorization", getAuthHeader())
                .GET()
                .build();

        HttpResponse<InputStream> response = client.send(request, HttpResponse.BodyHandlers.ofInputStream());

        if (response.statusCode() == 200) {
            return response.body();
        } else {
            throw new Exception("Failed to fetch document. Status code: " + response.statusCode());
        }
    }

	//check if docuemnt exist in boond :	
	public JsonNode getOppBoond(String endpoint, int page, int limit) {
		  String paginatedEndpoint = String.format("%s?page=%d&maxResults=%d", baseUrl + endpoint, page, limit);

		    HttpRequest request = HttpRequest.newBuilder()
		            .uri(URI.create(paginatedEndpoint))
		            .header("Authorization", getAuthHeader())
		            .GET()
		            .build();

		    try {
		        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		        if (response.statusCode() == 200) {
		            // Parse the response body as JsonNode
		            JsonNode responseJson = objectMapper.readTree(response.body());

		            // Check if the opportunity contains files
		            JsonNode filesNode = responseJson.path("data").path("relationships").path("files").path("data");

		            if (filesNode.isArray() && filesNode.size() > 0) {
		                // Opportunity contains files/documents
		                return responseJson;
		            } else {
		                // Opportunity does not contain any files/documents
		                throw new RuntimeException("This opportunity does not contain any documents. You may want to upload one.");
		                // Optionally, call uploadFileToOpportunity(opportunityId); here
		            }
		        } else {
		            throw new RuntimeException("Error fetching data from BoondManager. Status: " + response.statusCode());
		        }
		    } catch (Exception e) {
		        e.printStackTrace();
		        throw new RuntimeException("Error while making API request: " + e.getMessage());
		    }
		}

	/////////////////////// Begin Api get opportunity for DropDown list ////////////////////
//	public List<Map<String, Object>> getOpportunitiesDropdown(int page, int limit) {
//		  List<Map<String, Object>> dropdownOptions = new ArrayList<>();
//		    
//		    // Step 1: Fetch paginated list of opportunities
//		    JsonNode allOpportunities = getFromBoondManagerAPI("/opportunities", page, limit);
//		    JsonNode opportunitiesData = allOpportunities.path("data");
//		    
//		    if (opportunitiesData.isArray()) {
//		        // Fetch dictionary data once for all opportunities
//		        JsonNode dictionaryData = dictionaryService.getOpportunityAndResourceDictionaryAsJson();
//
//		        // Step 2: Use a thread pool for optimized parallelization
//		        ExecutorService executor = Executors.newFixedThreadPool(10); // Adjust thread pool size based on system capacity
//		        List<Callable<Map<String, Object>>> tasks = new ArrayList<>();
//		        
//		        // Step 3: Prepare async tasks for each opportunity
//		        for (JsonNode opportunityNode : opportunitiesData) {
//		            String opportunityId = opportunityNode.path("id").asText();
//
//		            tasks.add(() -> {
//		                Map<String, Object> opportunityDetails = new HashMap<>();
//		                
//		                // Fetch opportunity details
//		                JsonNode opportunityInfo = getOppByIdFromBoondManagerAPI("/opportunities/" + opportunityId + "/information");
//		                JsonNode attributes = opportunityInfo.at("/data/attributes");
//		                
//		                // Populate opportunity details
//		                opportunityDetails.put("id", opportunityInfo.at("/data/id").asText());
//		                opportunityDetails.put("type", opportunityInfo.at("/data/type").asText());
//		                opportunityDetails.put("creationDate", attributes.path("creationDate").asText());
//		                opportunityDetails.put("updateDate", attributes.path("updateDate").asText());
//		                opportunityDetails.put("title", attributes.path("title").asText());
//		                opportunityDetails.put("reference", attributes.path("reference").asText());
//		                opportunityDetails.put("startDate", attributes.path("startDate").asText());
//		                opportunityDetails.put("expertiseArea", attributes.path("expertiseArea").asText());
//		                opportunityDetails.put("activityAreas", attributes.path("activityAreas"));
//		                opportunityDetails.put("closingDate", attributes.path("closingDate").asText());
//		                
//		                // Enrich with state, type, activity area, expertise area
//		                opportunityDetails.put("stateValue", getValueFromDict(dictionaryData.path("opportunity"), attributes.path("state").asText(), "state"));
//		                opportunityDetails.put("typeOfValue", getValueFromDict(dictionaryData.path("typeOfProject"), attributes.path("typeOf").asText(), "typeOf"));
//		                opportunityDetails.put("activityArea", dictionaryData.path("activityArea").findValue(attributes.path("activityArea").asText()));
//		                opportunityDetails.put("expertiseAreaValue", dictionaryData.path("expertiseArea").findValue(attributes.path("expertiseArea").asText()));
//		                
//		                // Check for documents
//		                boolean hasDocument = opportunityInfo.path("data").path("relationships").path("files").path("data").size() > 0;
//		                
//		                // Fetch included resources in batch if possible
//		                List<Map<String, Object>> includedResources = fetchIncludedResources(opportunityInfo, dictionaryData);
//
//		                // Combine all data for this opportunity
//		                Map<String, Object> combinedResponse = new HashMap<>();
//		                combinedResponse.put("opportunityDetails", opportunityDetails);
//		                combinedResponse.put("includedResources", includedResources);
//		                combinedResponse.put("hasDocument", hasDocument);
//		                
//		                return combinedResponse;
//		            });
//		        }
//
//		        // Step 4: Execute all tasks in parallel and collect results
//		        try {
//		            List<Future<Map<String, Object>>> results = executor.invokeAll(tasks);
//		            for (Future<Map<String, Object>> result : results) {
//		                dropdownOptions.add(result.get());  // Wait for completion and collect the result
//		            }
//		        } catch (InterruptedException | ExecutionException e) {
//		            e.printStackTrace();
//		        } finally {
//		            executor.shutdown(); // Always shut down the executor
//		        }
//		    }
//
//		    return dropdownOptions;
//		}

	private List<Map<String, Object>> fetchIncludedResources(JsonNode opportunityInfo, JsonNode dictionaryData) {
	    List<Map<String, Object>> includedResources = new ArrayList<>();

	    for (JsonNode includedNode : opportunityInfo.withArray("included")) {
	        Map<String, Object> resourceDetails = new HashMap<>();
	        resourceDetails.put("id", includedNode.path("id").asText());
	        resourceDetails.put("type", includedNode.path("type").asText());
	        resourceDetails.put("attributes", includedNode.path("attributes"));

	        if ("resource".equals(includedNode.path("type").asText())) {
	            String resourceId = includedNode.path("id").asText();
	            JsonNode resourceResponse = getRessourceFromBoondManagerAPI("/resources/" + resourceId, 1, 100);

	            if (resourceResponse != null && resourceResponse.path("data").isObject()) {
	                JsonNode resourceAttributes = resourceResponse.path("data").path("attributes");

	                // Extract the thumbnail
	                String thumbnail = resourceAttributes.path("thumbnail").asText(null);
	                if (thumbnail != null) {
	                    try {
	                        // Fetch the actual thumbnail as byte array
	                        byte[] thumbnailData = fetchThumbnail(thumbnail);

	                        // Add thumbnail data to the response
	                        resourceDetails.put("thumbnail", thumbnailData);
	                    } catch (RuntimeException e) {
	                        resourceDetails.put("thumbnail", null); // Handle errors gracefully
	                    }
	                }
	                // Extract resource type information
	                String resourceTypeOfValueFromAPI = getValueFromDict(
	                        dictionaryData.path("resource"), 
	                        resourceAttributes.path("typeOf").asText(), 
	                        "typeOf"
	                );
	                resourceDetails.put("resourceTypeOfValue", resourceTypeOfValueFromAPI);
	            }
	        }
	        // Fetch additional email data for all resources
	        JsonNode allResourcesResponse = getFromBoondManagerAPI("/resources", 1, 100);
	        if (allResourcesResponse != null && allResourcesResponse.path("data").isArray()) {
	            for (JsonNode resourceNode : allResourcesResponse.path("data")) {
	                if (resourceNode.path("id").asText().equals(includedNode.path("id").asText())) {
	                    String additionalEmail = resourceNode.path("attributes").path("email1").asText(null);
	                    if (additionalEmail != null) {
	                        resourceDetails.put("additionalEmail", additionalEmail);
	                    }
	                    break;
	                }
	            }
	        }
	        includedResources.add(resourceDetails);
	    }

	    return includedResources;
	}

	private String getValueFromDict(JsonNode dictData, String id, String fieldName) {
		    if (dictData.isArray()) {
		        for (JsonNode entry : dictData) {
		            if (entry.path("id").asText().equals(id)) {
		                return entry.path("value").asText();
		            }
		        }
		    }
		    return "Unknown"; // Default value if not found
		}

	
	/////////////////////// END Api get opportunity for DropDown list ////////////////////
	
	//Fetch Ressource profile  from boond		
	public byte[] fetchThumbnail(String id) {
		        String endpoint = "/thumbnails/" + id;

		        try {
		            HttpRequest request = HttpRequest.newBuilder()
		                    .uri(URI.create(baseUrl + endpoint))
		                    .header("Authorization", getAuthHeader())
		                    .GET()
		                    .build();

		            HttpResponse<byte[]> response = client.send(request, HttpResponse.BodyHandlers.ofByteArray());

		            if (response.statusCode() == 200) {
		                return response.body();
		            } else {
		                throw new RuntimeException("Error fetching thumbnail. Status: " + response.statusCode());
		            }
		        } catch (Exception e) {
		            throw new RuntimeException("Error while making API request: " + e.getMessage(), e);
		        }
		    }
	
	
	// Get Opportunities with Documents only
	public Map<String, Object> getOpportunitiesWithDocuments(int page, int limit) {
	    Map<String, Object> response = new LinkedHashMap<>(); // Use LinkedHashMap to preserve insertion order
	    List<Map<String, Object>> opportunitiesWithDocuments = new ArrayList<>();

	    try {
	        // Step 1: Fetch paginated list of opportunities
	        JsonNode allOpportunities = getFromBoondManagerAPI("/opportunities", page, limit);
	        JsonNode opportunitiesData = allOpportunities.path("data");

	        if (opportunitiesData.isArray()) {
	            // Step 2: Use a thread pool for optimized parallelization
	            ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
	            List<Callable<Map<String, Object>>> tasks = new ArrayList<>();

	            // Step 3: Prepare async tasks for each opportunity
	            for (JsonNode opportunityNode : opportunitiesData) {
	                String opportunityId = opportunityNode.path("id").asText();

	                tasks.add(() -> {
	                    JsonNode opportunityInfo = getOppByIdFromBoondManagerAPI("/opportunities/" + opportunityId + "/information");

	                    // Check if documents are associated
	                    JsonNode documentsNode = opportunityInfo.path("data").path("relationships").path("files").path("data");
	                    if (documentsNode.isArray() && documentsNode.size() > 0) {
	                        // Populate response if the opportunity has documents
	                        Map<String, Object> opportunityDetails = new HashMap<>();
	                        opportunityDetails.put("opportunityId", opportunityId);

	                        // Get the first document ID (assuming multiple documents are possible)
	                        String documentId = documentsNode.get(0).path("id").asText();
	                        opportunityDetails.put("documentId", documentId);
	                        opportunityDetails.put("hasDocument", true);

	                        return opportunityDetails; // Return filtered data
	                    }
	                    return null; // Skip opportunities without documents
	                });
	            }

	            // Step 4: Execute all tasks in parallel and collect results
	            List<Future<Map<String, Object>>> results = executor.invokeAll(tasks);
	            for (Future<Map<String, Object>> result : results) {
	                try {
	                    Map<String, Object> opportunityData = result.get();
	                    if (opportunityData != null) { // Exclude null results
	                        opportunitiesWithDocuments.add(opportunityData);
	                    }
	                } catch (ExecutionException e) {
	                    e.printStackTrace();
	                }
	            }

	            executor.shutdown(); // Always shut down the executor
	        }

	        // Step 5: Prepare response
	        response.put("status", "SUCCESS"); // Status comes before data
	        response.put("data", opportunitiesWithDocuments);

	    } catch (Exception e) {
	        e.printStackTrace();
	        response.put("status", "FAILURE");
	        response.put("message", "Error fetching opportunities with documents");
	    }

	    return response;
	}

	
	public Map<String, Object> getOpportunityDetailsById(String opportunityId) {
	    Map<String, Object> response = new LinkedHashMap<>(); // Use LinkedHashMap to maintain order
	
	    try {
	        // Fetch opportunity details from BoondManager API
	        JsonNode opportunityInfo = getOppByIdFromBoondManagerAPI("/opportunities/" + opportunityId + "/information");
	
	        // Validate the response
	        if (opportunityInfo == null || !opportunityInfo.has("data")) {
	            response.put("status", "FAILURE");
	            response.put("message", "Opportunity not found for ID: " + opportunityId);
	            return response;
	        }
	
	        // Extract opportunity attributes
	        JsonNode attributes = opportunityInfo.at("/data/attributes");
	        Map<String, Object> opportunityDetails = new LinkedHashMap<>();
	
	        // Populate opportunity details
	        populateOpportunityDetails(opportunityDetails, opportunityInfo, attributes);
	
	        // Fetch dictionary data once for enrichment
	        JsonNode dictionaryData = dictionaryService.getOpportunityAndResourceDictionaryAsJson();
	
	        // Enrich with dictionary values
	        enrichOpportunityDetails(opportunityDetails, dictionaryData, attributes);
	
	        // Check if documents are associated
	        opportunityDetails.put("hasDocument", hasAssociatedDocuments(opportunityInfo));
	
	        // Fetch included resources
	        List<Map<String, Object>> includedResources = fetchIncludedResources(opportunityInfo, dictionaryData);
	        opportunityDetails.put("includedResources", includedResources);
	
	        // Structure the response
	        response.put("status", "SUCCESS");
	        response.put("data", opportunityDetails);
	
	    } catch (Exception e) {
	        // Handle unexpected errors
	        response.put("status", "FAILURE");
	        response.put("message", "An error occurred while fetching opportunity details for ID: " + opportunityId);
	        System.err.println("Error fetching opportunity details for ID: " + opportunityId);
	        e.printStackTrace();
	    }
	
	    return response;
	}

	
	
	
	private void populateOpportunityDetails(Map<String, Object> opportunityDetails, JsonNode opportunityInfo, JsonNode attributes) {
	    opportunityDetails.put("id", opportunityInfo.at("/data/id").asText());
	    opportunityDetails.put("type", opportunityInfo.at("/data/type").asText());
	    opportunityDetails.put("creationDate", attributes.path("creationDate").asText(""));
	    opportunityDetails.put("updateDate", attributes.path("updateDate").asText(""));
	    opportunityDetails.put("title", attributes.path("title").asText(""));
	    opportunityDetails.put("reference", attributes.path("reference").asText(""));
	    opportunityDetails.put("startDate", attributes.path("startDate").asText(""));
	    opportunityDetails.put("expertiseArea", attributes.path("expertiseArea").asText(""));
	    opportunityDetails.put("activityAreas", attributes.path("activityAreas"));
	    opportunityDetails.put("closingDate", attributes.path("closingDate").asText(""));
	}

	private void enrichOpportunityDetails(Map<String, Object> opportunityDetails, JsonNode dictionaryData, JsonNode attributes) {
	    opportunityDetails.put("stateValue", getValueFromDict(dictionaryData.path("opportunity"), attributes.path("state").asText(), "state"));
	    opportunityDetails.put("typeOfValue", getValueFromDict(dictionaryData.path("typeOfProject"), attributes.path("typeOf").asText(), "typeOf"));
	    opportunityDetails.put("activityArea", dictionaryData.path("activityArea").findValue(attributes.path("activityArea").asText()));
	    opportunityDetails.put("expertiseAreaValue", dictionaryData.path("expertiseArea").findValue(attributes.path("expertiseArea").asText()));
	}

	private boolean hasAssociatedDocuments(JsonNode opportunityInfo) {
	    return opportunityInfo.path("data").path("relationships").path("files").path("data").size() > 0;
	}


	
}
