package com.example.AIGen.payload.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public class OtpRequest {
	private String email;
	private String level;
	private String firstName;
	private String lastName; 
	private String resourceId;
	private String otp;
	
	public OtpRequest(String email, String level, String firstName, String lastName, String resourceId) {
		this.email=email;
		this.level=level;
		this.firstName=firstName;
		this.lastName=lastName;
		this.resourceId=resourceId;
		// TODO Auto-generated constructor stub
	}
	
	
	public String getOtp() {
		return otp;
	}


	public void setOtp(String otp) {
		this.otp = otp;
	}


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
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getResourceId() {
		return resourceId;
	}
	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}
	
	
    
}
