package com.example.AIGen.Controllers;





import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.AIGen.Dto.ResumesDTO;
import com.example.AIGen.services.ResumesService;

@RestController
@RequestMapping("/api/resumes")
public class ResumesController {

	@Autowired
	private ResumesService resumesService;

	
//    @PostMapping("/addresumes")
//    public ResponseEntity<ResumesDTO> createResume(@RequestBody ResumesDTO resumesDTO) {
//    	ResumesDTO savedResume = resumesService.createResume(resumesDTO);
//        return ResponseEntity.ok(savedResume);
//    }

//    @GetMapping("/{_id}")
//    public ResponseEntity<ResumesDTO> getResumeById(@PathVariable String _id) {
//        ResumesDTO resume = resumesService.getResumeById(_id);
//        return ResponseEntity.ok(resume);
//    }
//    @GetMapping("/getallresumes")
//    public ResponseEntity<List<ResumesDTO>> getAllResumes() {
//        List<ResumesDTO> resumes = resumesService.getAllResumes();
//        return ResponseEntity.ok(resumes);
//    }
}
