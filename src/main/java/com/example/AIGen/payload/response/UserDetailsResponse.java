package com.example.AIGen.payload.response;

import com.example.AIGen.models.User;

public class UserDetailsResponse {
	
	 	private String status;
	    private Data data;
	    private String message;

	    // Constructor
	    public UserDetailsResponse(String status, String message, Data data) {
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

	    public Data getData() {
	        return data;
	    }

	    public void setData(Data data) {
	        this.data = data;
	    }

	    public String getMessage() {
	        return message;
	    }

	    public void setMessage(String message) {
	        this.message = message;
	    }

	    // Inner class for Data
	    public static class Data {
	        private User userDetails;

	        public Data(User userDetails) {
	            this.userDetails = userDetails;
	        }

	        public User getUserDetails() {
	            return userDetails;
	        }

	        public void setUserDetails(User userDetails) {
	            this.userDetails = userDetails;
	        }
	    }
}
