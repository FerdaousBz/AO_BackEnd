package com.example.AIGen.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.AIGen.Dto.ActivityAreaDTO;
import com.example.AIGen.Dto.ExpertiseAreaDTO;
import com.example.AIGen.Dto.StateDTO;
import com.example.AIGen.Dto.TypeOfDTO;
import com.example.AIGen.services.BoondManagerAPIClient;
import com.example.AIGen.services.DictionaryService;
import com.fasterxml.jackson.databind.JsonNode;

@RestController
@RequestMapping("/boondmanager/dictionary")
@CrossOrigin
public class DictionaryController {

	@Autowired
	private DictionaryService dictionaryService;
	
	@Autowired
	BoondManagerAPIClient boondManagerAPIClient;
	
	//get dectionary value
    @GetMapping("/value")
    public ResponseEntity<JsonNode> getDictionaryValue() {
        JsonNode result = dictionaryService.getOpportunityAndResourceDictionaryAsJson();
        return ResponseEntity.ok(result);
    }
    
    //get value dectionary for an opportunity
    @GetMapping("/opportunities/{id}")
    public JsonNode getOpportunityByIdWithDictionary(@PathVariable String id) {
        // Call service to get opportunity and enrich with dictionary values
        return dictionaryService.getOpportunityByIdWithDictionary(id);
    }
    @GetMapping("/opportunities/filter")
    public JsonNode getFilteredOpportunities(@RequestParam(required = false) String typeOfFilter,
            @RequestParam(required = false) String expertiseAreaFilter) {
			// Call the service method to get filtered opportunities
			return dictionaryService.filterOpportunities(typeOfFilter, expertiseAreaFilter);
			}

}


