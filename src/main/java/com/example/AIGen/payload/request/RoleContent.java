package com.example.AIGen.payload.request;

public class RoleContent {
	
	private String role;
    private String content;
    
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public RoleContent(String role, String content) {
		super();
		this.role = role;
		this.content = content;
	}
    
    

}
