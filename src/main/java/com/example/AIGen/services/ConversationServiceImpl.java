package com.example.AIGen.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.AIGen.Repository.ConversationRepository;
import com.example.AIGen.Repository.MessageRepository;
import com.example.AIGen.Repository.UserRepository;
import com.example.AIGen.models.Conversation;
import com.example.AIGen.models.Message;
import com.example.AIGen.payload.request.PromptRequest;
import com.example.AIGen.payload.request.RoleContent;

@Service
public class ConversationServiceImpl implements ConversationService {
	
	@Autowired
    private ConversationRepository conversationRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userService;
    
    @Autowired
	private AIService aiService;




	@Override
	public String addMessageToConversation(String userId, String conversationId, Message message) {
		  Optional<Conversation> conversationOpt = conversationRepository.findById(conversationId);
		    
		  if (conversationOpt.isPresent()) {
		        Conversation conversation = conversationOpt.get();
		        System.out.println("Adding message to conversation: " + conversationId);

		        // Set the timestamp and save the message
		        message.setTimestamp(new Date());
		        Message savedMessage = messageRepository.save(message);  // Save the message to the repository
		        
		        // Log the message being added
		        System.out.println("Message content: " + message.getContent());
		        
		        // Add the saved message to the conversation's messageIds list
		        conversation.getMessageIds().add(savedMessage);
		        conversationRepository.save(conversation);
		        
		        // Returning detailed output
		        //return String.format("Message added successfully: %s", savedMessage.getContent());
		        return savedMessage.getContent();
		    } else {
		        System.out.println("Conversation not found for ID: " + conversationId);
		        return "Conversation not found";
		    }
		    
	}

	@Override
	public List<Message> getMessages(List<String> messagesIds) {
		
//		Optional<Conversation> conversationOpt = conversationRepository.findById(conversationId);
//        if (conversationOpt.isPresent()) {
//            Conversation conversation = conversationOpt.get();
//            List<Message> messages = new ArrayList<>();
//            for (String messageId : conversation.getMessageIds()) {
//                messages.add(messageRepository.findById(messageId).orElse(null));
//            }
//            return messages;
//        }
        return new ArrayList<>();
	}

	@Override
	public List<Conversation> getConversationsByUserId(String userId) {
        return conversationRepository.findByUserId(userId);
	}

	@Override
	public Optional<Conversation> getConversationById(String conversationId) {
		 return conversationRepository.findById(conversationId);
	}

	@Override
	public Conversation saveConversation(Conversation conversation) {
		 return conversationRepository.save(conversation);
	}
	
	private PromptRequest generatePrompt(Conversation conversation) {
        PromptRequest promptRequest = new PromptRequest();
        List<RoleContent> prompt = new ArrayList<>();

        // Retrieve all messages in the conversation
        List<Message> messages = conversation.getMessageIds();

        // Sort messages by date
        Collections.sort(messages, Comparator.comparing(Message::getTimestamp));

        // Add the system message only if it's the first message in the conversation
        if (messages.size() == 1) {
            prompt.add(new RoleContent("system", "you will answer about everything"));
        }

        for (Message msg : messages) {
            prompt.add(new RoleContent(msg.getSender(), msg.getContent()));
        }

        promptRequest.setPrompt(prompt);
        return promptRequest;
    }

}
