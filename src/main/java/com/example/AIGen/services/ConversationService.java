package com.example.AIGen.services;

import java.util.List;
import java.util.Optional;

import com.example.AIGen.models.Conversation;
import com.example.AIGen.models.Message;

public interface ConversationService {
	
	public List<Conversation> getConversationsByUserId(String userId) ;
	public List<Message> getMessages(List<String> messagesIds) ;
	public Optional<Conversation> getConversationById(String conversationId);
	public Conversation saveConversation(Conversation conversation);
	public String addMessageToConversation(String conversationId, String userId, Message message);

}
