package com.example.AIGen.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.AIGen.payload.request.PromptRequest;
import com.example.AIGen.services.AIService;
import com.example.AIGen.services.ConversationService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
public class TestController {
	
	@Autowired
    private AIService aiService;
	
	@GetMapping("/all")
	public String allAccess() {
		return "Public Content.";
	}
	
	@PostMapping("/user")
	public String userAccess(@RequestBody PromptRequest prompt) {
		return aiService.getAIResponse(prompt);
	}

	@GetMapping("/mod")
	//@PreAuthorize("hasRole('MODERATOR')")
	public String moderatorAccess() {
		return "Moderator Board.";
	}

	@GetMapping("/admin")
	//@PreAuthorize("hasRole('ADMIN')")
	public String adminAccess() {
		return "Admin Board.";
	}
}