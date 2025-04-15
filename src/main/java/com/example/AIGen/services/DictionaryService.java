package com.example.AIGen.services;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.AIGen.Dto.ActivityAreaDTO;
import com.example.AIGen.Dto.ExpertiseAreaDTO;
import com.example.AIGen.Dto.StateDTO;
import com.example.AIGen.Dto.TypeOfDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Service
public class DictionaryService {
	 @Value("${boondmanager.api.base-url}")
	    private String baseUrl;

	    @Value("${boondmanager.ui.username}")
	    private String username;

	    @Value("${boondmanager.ui.password}")
	    private String password;

	    @Autowired
	    private BoondManagerAPIClient boondManagerAPIClient;

	    @Autowired
	    private FileUploadService fileuploadService;
	    
	    private final WebClient webClient;
	    private final HttpClient client = HttpClient.newHttpClient();
	    private final ObjectMapper objectMapper = new ObjectMapper();

	    public DictionaryService(WebClient.Builder webClientBuilder) {
	        this.webClient = webClientBuilder.baseUrl(baseUrl + "/application/dictionary").build();
	    }

	    private String getAuthHeader() {
	        String auth = username + ":" + password;
	        return "Basic " + Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
	    }

	    //get the whole dictionary
	    public JsonNode getDictionaryData() {
	        HttpRequest request = HttpRequest.newBuilder()
	                .uri(URI.create(baseUrl + "/application/dictionary"))
	                .header("Authorization", getAuthHeader())
	                .GET()
	                .build();

	        try {
	            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
	            if (response.statusCode() == 200) {
	                return objectMapper.readTree(response.body());
	            } else {
	                throw new RuntimeException("Error fetching dictionary data. Status: " + response.statusCode());
	            }
	        } catch (Exception e) {
	            throw new RuntimeException("Error while making dictionary API request: " + e.getMessage(), e);
	        }
	    }
	    
	    //Get Dectionary value for such ArrayNode  type 
		public JsonNode getOpportunityAndResourceDictionaryAsJson() {
		   
		    JsonNode dictionaryData = getDictionaryData();
		
		    // Step 2: Initialize arrays to store the extracted data
		    ArrayNode opportunityArray = objectMapper.createArrayNode();
		    ArrayNode resourceArray = objectMapper.createArrayNode();
		    ArrayNode typeOfOppArray = objectMapper.createArrayNode();
		    ArrayNode activityAreaArray = objectMapper.createArrayNode();
		    ArrayNode expertiseArea = objectMapper.createArrayNode();
		    
		    // Step 3: Extract opportunity state data from dictionary
		    JsonNode opportunityData = dictionaryData.at("/data/setting/state/opportunity");
		    if (opportunityData.isArray()) {
		        for (JsonNode entry : opportunityData) {
		            ObjectNode opportunityEntry = objectMapper.createObjectNode();
		            opportunityEntry.put("id", entry.path("id").asText());
		            opportunityEntry.put("value", entry.path("value").asText());
		            opportunityArray.add(opportunityEntry);
		        }
		    }
		
		    // Step 4: Extract typeOf/resource data from dictionary
		    JsonNode resourceData = dictionaryData.at("/data/setting/typeOf/resource");
		    if (resourceData.isArray()) {
		        for (JsonNode entry : resourceData) {
		            ObjectNode resourceEntry = objectMapper.createObjectNode();
		            resourceEntry.put("id", entry.path("id").asText());
		            resourceEntry.put("value", entry.path("value").asText());
		            resourceArray.add(resourceEntry);
		        }
		    }
		
		    // Step 5: Extract typeOf/project data from dictionary
		    JsonNode typeOfOppData = dictionaryData.at("/data/setting/typeOf/project");
		    if (typeOfOppData.isArray()) {
		        for (JsonNode entry : typeOfOppData) {
		            ObjectNode typeOfEntry = objectMapper.createObjectNode();
		            typeOfEntry.put("id", entry.path("id").asText());
		            typeOfEntry.put("value", entry.path("value").asText());
		            typeOfOppArray.add(typeOfEntry);
		        }
		    }

		    // Step 6: Extract activityArea data from dictionary
		    JsonNode activityAreaData = dictionaryData.at("/data/setting/activityArea");
		    if (activityAreaData.isArray()) {
		        for (JsonNode entry : activityAreaData) {
		            String id = entry.path("id").asText();

		            if ("Domaine".equals(id)) {
		                ObjectNode activAreaEntry = objectMapper.createObjectNode();
		                activAreaEntry.put("id", id);

		                // Create an ArrayNode to store the extracted options
		                ArrayNode optionsArray = objectMapper.createArrayNode();

		                // Iterate over the options array inside each entry
		                JsonNode options = entry.path("option");
		                if (options.isArray()) {
		                    for (JsonNode option : options) {
		                        ObjectNode optionEntry = objectMapper.createObjectNode();
		                        optionEntry.put("id", option.path("id").asText());
		                        optionEntry.put("value", option.path("value").asText());
		                        optionsArray.add(optionEntry);  // Add each option object to the options array
		                    }
		                }

		            // Add the options array to the activity area entry
		            activAreaEntry.set("option", optionsArray);
		            activityAreaArray.add(activAreaEntry);  // Add the complete activity area entry to the main array
		        }
		    }
		  }
		 // Step 7: Extract expertiseArea data from dictionary
		    JsonNode expertiseAreaData = dictionaryData.at("/data/setting/expertiseArea");
		    if (expertiseAreaData.isArray()) {
		        for (JsonNode entry : expertiseAreaData) {
		            ObjectNode expertiseAreaEntry = objectMapper.createObjectNode();
		            expertiseAreaEntry.put("id", entry.path("id").asText());
		            expertiseAreaEntry.put("value", entry.path("value").asText());

		            // Add the expertise area entry to the main expertiseArea array
		            expertiseArea.add(expertiseAreaEntry);
		        }
		    }
		    // Step 8: Create the final JSON structure
		    ObjectNode result = objectMapper.createObjectNode();
			    result.set("opportunity", opportunityArray);
			    result.set("resource", resourceArray);
			    result.set("typeOfProject", typeOfOppArray);
			    result.set("activityArea", activityAreaArray);
			    result.set("expertiseArea",expertiseArea);
		    return result;
		}

		// This method enriches the opportunity data with the corresponding values from the dictionary.
		public JsonNode getOpportunityByIdWithDictionary(String opportunityId) {
		    // Step 1: Fetch opportunity data by ID from BoondManager API
		    JsonNode opportunityData = boondManagerAPIClient.getOppByIdFromBoondManagerAPI("/opportunities/" + opportunityId + "/information");
		    // Step 2: Get the dictionary data
		    JsonNode dictionaryData = getOpportunityAndResourceDictionaryAsJson();
		
		    // Step 3: Enrich the opportunity with dictionary values
		    ObjectNode enrichedOpportunity = objectMapper.createObjectNode();
		    enrichedOpportunity.set("opportunity", opportunityData);
		
		    // Enrich the 'state' field using the opportunityStateDict
		    JsonNode opportunityStateDict = dictionaryData.path("opportunity");
		    String stateValue = getValueFromDict(opportunityStateDict, opportunityData.path("data").path("attributes").path("state").asText(), "state");
		    enrichedOpportunity.put("stateValue", stateValue);
		
		    // Enrich the 'typeOf' field using the typeOfProjectDict
		    JsonNode typeOfProjectDict = dictionaryData.path("typeOfProject");
		    String typeOfValue = getValueFromDict(typeOfProjectDict, opportunityData.path("data").path("attributes").path("typeOf").asText(), "typeOf");
		    enrichedOpportunity.put("typeOfValue", typeOfValue);
		
		    // Step 4: Fetch and enrich resource details
		    JsonNode resourceDict = dictionaryData.path("resource");
		    JsonNode includedResources = opportunityData.path("included");
		
		    // Assuming the resource ID is in the included data, search for it
		    if (includedResources.isArray()) {
		        for (JsonNode includedResource : includedResources) {
		            if ("resource".equals(includedResource.path("type").asText())) {
		                String resourceId = includedResource.path("id").asText();
		
		                // Call API to get resource details
		                JsonNode resourceResponse = boondManagerAPIClient.getRessourceFromBoondManagerAPI("/resources/" + resourceId, 1, 100);
		                if (resourceResponse != null && resourceResponse.path("data").isObject()) {
		                    JsonNode resourceAttributes = resourceResponse.path("data").path("attributes");
		
		                    // Extract and enrich the `typeOf` field for the resource
		                    String resourceTypeOfId = resourceAttributes.path("typeOf").asText();
		                    String resourceTypeOfValue = getValueFromDict(resourceDict, resourceTypeOfId, "typeOf");
		                    enrichedOpportunity.put("resourceTypeOfValue", resourceTypeOfValue);
		                }
		                break;  // Assuming only one resource ID is needed; exit the loop after processing
		            }
		        }
		    }
         

		    // Enrich other fields like activityArea and expertiseArea as previously done
		    JsonNode activityAreaDict = dictionaryData.path("activityArea");
		    String activityAreaValue = getValueFromDict(activityAreaDict, opportunityData.path("data").path("attributes").path("activityAreas").asText(),"activityAreas");
		    enrichedOpportunity.put("activityAreaValue", activityAreaValue);
		
		    JsonNode expertiseAreaDict = dictionaryData.path("expertiseArea");
		    String expertiseAreaValue = getValueFromDict(expertiseAreaDict, opportunityData.path("data").path("attributes").path("expertiseArea").asText(), "expertiseArea");
		    enrichedOpportunity.put("expertiseAreaValue", expertiseAreaValue);
		
		    return enrichedOpportunity;
		}
	

		// Helper method to fetch the value from the dictionary by matching the ID
		private String getValueFromDict(JsonNode dictData, String id, String fieldName) {
		    if (dictData.isArray()) {
		        for (JsonNode entry : dictData) {
		            // Compare the ID values as text since IDs are typically strings
		            if (entry.path("id").asText().equals(id)) {
		                return entry.path("value").asText(); // Return the value associated with the matching ID
		            }
		        }
		    }
		    return "Unknown"; // If no matching ID is found, return a default value
		}
	
		public JsonNode filterOpportunities(String expertiseAreaFilter, String typeOfFilter) {
		    JsonNode dictionaryData = getDictionaryData();
		    
		    // Initialize arrays to store the extracted data
		    ArrayNode typeOfOppArray = objectMapper.createArrayNode();
		    ArrayNode expertiseAreaArray = objectMapper.createArrayNode();

		    // Step 5: Extract typeOf/project data from dictionary
		    JsonNode typeOfOppData = dictionaryData.at("/data/setting/typeOf/project");
		    if (typeOfOppData.isArray()) {
		        for (JsonNode entry : typeOfOppData) {
		            ObjectNode typeOfEntry = objectMapper.createObjectNode();
		            typeOfEntry.put("id", entry.path("id").asText());
		            typeOfEntry.put("value", entry.path("value").asText());

		            // Apply filter for typeOf
		            if (typeOfFilter == null || typeOfFilter.equalsIgnoreCase(entry.path("value").asText())) {
		                typeOfOppArray.add(typeOfEntry);
		            }
		        }
		    }

		    // Step 7: Extract expertiseArea data from dictionary
		    JsonNode expertiseAreaData = dictionaryData.at("/data/setting/expertiseArea");
		    if (expertiseAreaData.isArray()) {
		        for (JsonNode entry : expertiseAreaData) {
		            ObjectNode expertiseAreaEntry = objectMapper.createObjectNode();
		            expertiseAreaEntry.put("id", entry.path("id").asText());
		            expertiseAreaEntry.put("value", entry.path("value").asText());

		            // Apply filter for expertiseArea
		            if (expertiseAreaFilter == null || expertiseAreaFilter.equalsIgnoreCase(entry.path("value").asText())) {
		                expertiseAreaArray.add(expertiseAreaEntry);
		            }
		        }
		    }

		    // Step 8: Create the final JSON structure
		    ObjectNode result = objectMapper.createObjectNode();
		    result.set("typeOfProject", typeOfOppArray);
		    result.set("expertiseArea", expertiseAreaArray);

		    return result;
		}






}