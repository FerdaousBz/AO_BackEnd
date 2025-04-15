package com.example.AIGen.payload.request;

import com.example.AIGen.models.Message;

public class MessageRequest {
    private String userId;
    private Message message;
    
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public Message getMessage() {
		return message;
	}
	public void setMessage(Message message) {
		this.message = message;
	}

    
}
