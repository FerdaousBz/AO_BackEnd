package com.example.AIGen.payload.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class PasswordUpdateRequest {

//    @Email
//    private String email;


    @Size(min = 8, message = "Password should have at least 8 characters")
    private String newPassword;
    
  
    @Size(min = 8, message = "Password should have at least 8 characters")
   private  String currentPassword;
//    @NotBlank
//    @Size(min = 8, message = "Password should have at least 8 characters")
//    private String confirmPassword;

    @NotBlank
    private String accessToken	; 
    


//	public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }

    public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

	public String getCurrentPassword() {
		return currentPassword;
	}

	public void setCurrentPassword(String currentPassword) {
		this.currentPassword = currentPassword;
	}

//    public String getConfirmPassword() {
//        return confirmPassword;
//    }
//
//    public void setConfirmPassword(String confirmPassword) {
//        this.confirmPassword = confirmPassword;
//    }
}
