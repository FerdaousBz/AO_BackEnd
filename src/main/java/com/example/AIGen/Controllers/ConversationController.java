package com.example.AIGen.Controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.AIGen.models.Conversation;
import com.example.AIGen.models.Message;
import com.example.AIGen.services.AIService;
import com.example.AIGen.services.ConversationService;

@RestController
@RequestMapping("/conversations")
public class ConversationController {
    
	@Autowired
    private ConversationService conversationService;
	

 

    @GetMapping("/user/{userId}")
    public List<Conversation> getConversations(@PathVariable String userId) {
        return conversationService.getConversationsByUserId(userId);
    }

    @GetMapping("/{conversationId}")
    public Optional<Conversation> getConversation(@PathVariable String conversationId) {
        return conversationService.getConversationById(conversationId);
    }

    @PostMapping
    public Conversation createConversation(@RequestBody Conversation conversation) {
        return conversationService.saveConversation(conversation);
    }
    
    
    @PostMapping("/{userId}/messages/{conversationId}")
    public ResponseEntity<Map<String, String>> addMessage(@PathVariable String conversationId, @PathVariable String userId,@RequestBody Message message) {
       
   	String response =conversationService.addMessageToConversation(conversationId, userId, message);
   	
    Map<String, String> jsonResponse = new HashMap<>();
    jsonResponse.put("message", response);
    
    // Return the response with appropriate HTTP status
    if (response.equals("Message added successfully")) {
        return ResponseEntity.ok(jsonResponse); // 200 OK
    } else {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(jsonResponse); // 404 Not Found
    }
}

}
