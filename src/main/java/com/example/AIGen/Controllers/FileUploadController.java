package com.example.AIGen.Controllers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.AIGen.payload.response.ApiResponse;
import com.example.AIGen.services.EmailService;
import com.example.AIGen.services.FileUploadService;
import com.example.AIGen.services.PowerPointService;

@RestController
@RequestMapping("/api/files")
@CrossOrigin
public class FileUploadController {

	@Autowired
	private FileUploadService fileUploadService;
	
   @Autowired
    private PowerPointService powerPointService;
	   
    @Autowired
    public FileUploadController(FileUploadService fileUploadService) {
        this.fileUploadService = fileUploadService;
    }
    
    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        File convFile = new File(file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convFile)) {
            fos.write(file.getBytes());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error saving file to disk: " + e.getMessage());
        }

        return fileUploadService.uploadFile(convFile);
    }


	 @PostMapping("/generate-summary")
	 public ResponseEntity<?> generateSummary(@RequestBody Map<String, String> request) {
	     String fileId = request.get("fileId");

	     // Validate fileId
	     if (fileId == null || fileId.isEmpty()) {
	         return ResponseEntity.badRequest().body(Collections.singletonMap("error", "fileId is required"));
	     }

	     // Call the service method to generate summary and return the JSON response
	     return fileUploadService.generateSummary(fileId);
	 }

	 
	 @GetMapping("/get-resume/{fileId}")
	 public ResponseEntity<?> getResume(@PathVariable String fileId) {
		 return fileUploadService.getResumeByFileId(fileId);


	 }
	 
	 //API combined the opportunities and generate summary to Generate summary for opportunities 
	 @GetMapping("/opportunity/{id}/generate-summary")
	 public ResponseEntity<?> generateSummaryForOpportunity(@PathVariable String id) {
	     // Call the service method that combines both APIs
	     return fileUploadService.generateSummaryForOpportunity(id);
	 }
	 
	 


}
