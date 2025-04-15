package com.example.AIGen.models;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "conversations")
public class Conversation {
	
    @Id
    private String id;
    private List<Message> messageIds;
    private String userId;
    
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public List<Message> getMessageIds() {
		return messageIds;
	}
	public void setMessageIds(List<Message> messageIds) {
		this.messageIds = messageIds;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}

   
}
