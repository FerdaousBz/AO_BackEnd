package com.example.AIGen.Controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.AIGen.Exception.PptGenerationException;
import com.example.AIGen.payload.response.ApiResponse;
import com.example.AIGen.payload.response.PptGenratedResponse;
import com.example.AIGen.services.PowerPointService;
import com.fasterxml.jackson.databind.JsonNode;

@RestController
@RequestMapping("/api/ppt")
@CrossOrigin
public class PptGeneratedController {
	
	@Autowired
	private PowerPointService powerPointService;
	

	 public PptGeneratedController(PowerPointService powerPointService) {
	        this.powerPointService = powerPointService;
	    }
	 
	 @PostMapping("/generate/ppt/{fileId}")
	 public ApiResponse generatePpt(@PathVariable String fileId) {
		 try {
		        ApiResponse response = powerPointService.generatePptFromApi(fileId);
		        return response;

		    } catch (Exception ex) {
		        // General exception handler returning 500 error
		        ApiResponse errorResponse = new ApiResponse("FAILED", "Internal server error", null);
		        return errorResponse;
		    }
	 }
   
	 
	    
	    @GetMapping("/get/generate/ppt/{summary_id}")
	    public ApiResponse getGeneratedPpt(@PathVariable String summary_id) {
	    	try {
	            // Call the service method to get the PPT details
	    		 ApiResponse response = powerPointService.getPptFromApi(summary_id);

	            // Return the response from the service directly
	            return response;

	        } catch (Exception ex) {
	            // General exception handler returning 500 error
	            ApiResponse errorResponse = new ApiResponse("FAILED", "Internal server error", null);
	            return errorResponse;
	        }
	    }
    }
//@PostMapping("/generate/{summary_id}")
//public ResponseEntity<PptGenratedResponse> generatePpt(@PathVariable String summary_id) {
//	 try {
//		 PptGenratedResponse response = powerPointService.generatePptFromApi(summary_id);
//	        return ResponseEntity.ok(response);
//	    } catch (PptGenerationException ex) {
//	        // This will be handled by the GlobalExceptionHandler
//	        throw ex; 
//	    } catch (Exception ex) {
//	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//	                             .body(new PptGenratedResponse("500", "Internal server error", null));
//	    }
//}

