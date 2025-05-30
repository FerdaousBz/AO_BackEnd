package com.example.AIGen.payload.request;

import java.util.Set;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class SignUpRequest {
//    @Size(min = 3, max = 20)
//    private String username;
 
    @NotBlank
    @Size(max = 50)
    @Email
    private String email;
    
//    private Set<String> roles;
    
    @NotBlank
    @Size(min = 6, max = 40)
    private String password;
    
    @Size(min = 6, max = 40)
    private String confirmPassword;
  
    
    
    
    public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

//	public String getUsername() {
//        return username;
//    }
// 
//    public void setUsername(String username) {
//        this.username = username;
//    }
// 
    public String getEmail() {
        return email;
    }
 
    public void setEmail(String email) {
        this.email = email;
    }
 
    public String getPassword() {
        return password;
    }
 
    public void setPassword(String password) {
        this.password = password;
    }
    
//    public Set<String> getRoles() {
//      return this.roles;
//    }
//    
//    public void setRole(Set<String> roles) {
//      this.roles = roles;
//    }
}
