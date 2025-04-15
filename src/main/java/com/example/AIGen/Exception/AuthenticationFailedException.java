package com.example.AIGen.Exception;

public class AuthenticationFailedException extends RuntimeException {
	
	private String status;
	
    public AuthenticationFailedException(String message, String status) {
        super(message);
        this.status= status;
    }

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
    
    
}