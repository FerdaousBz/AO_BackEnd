package com.example.AIGen.payload.response;

import edu.stanford.nlp.patterns.Data;

public class ApiResponse {

	 	private String status;
	    private Object data;
	    private String message;

	    public ApiResponse(String status, Object data, String message) {
	        this.status = status;
	        this.data = data;
	        this.message = message;
	    }

	    // Getters and Setters
	    public String getStatus() {
	        return status;
	    }

	    public void setStatus(String status) {
	        this.status = status;
	    }

	    public Object getData() {
	        return data;
	    }

	    public void setData(Object data) {
	        this.data = data;
	    }

	    public String getMessage() {
	        return message;
	    }

	    public void setMessage(String message) {
	        this.message = message;
	    }

	    public static class Data {
	        private String email;
	        private String level;
			private String firstName;
			private String lastName;
			private String resourceId;
			
	        public Data(String email, String level) {
	            this.email = email;
	            this.level = level;
	        }

	        public Data(String email, String level, String firstName, String lastName, String resourceId) {
	        	this.email = email;
	        	this.level = level;
	        	this.firstName = firstName;
	        	this.lastName = lastName;
	        	this.resourceId=resourceId;
				// TODO Auto-generated constructor stub
			}

	

			// Getters and Setters
	        public String getEmail() {
	            return email;
	        }

	        public void setEmail(String email) {
	            this.email = email;
	        }

	        public String getLevel() {
	            return level;
	        }

	        public void setLevel(String level) {
	            this.level = level;
	        }
	    }
	    
}
