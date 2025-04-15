package com.example.AIGen.payload.response;

import java.util.List;

public class JwtResponse {
	private String accessToken;
	private String type = "Bearer";
	private String id;
	private String username;
	private String email;
	private String level;
	private String profileImageUrl;
	
	public JwtResponse(String accessToken, String id, String username, String email, String level,String profileImageUrl) {
		
		this.id = id;
		this.username = username;
		this.email = email;
		this.level = level;
		this.accessToken = accessToken;
		this.profileImageUrl =profileImageUrl;
	}

	
	public String getProfileImageUrl() {
		return profileImageUrl;
	}


	public void setProfileImageUrl(String profileImageUrl) {
		this.profileImageUrl = profileImageUrl;
	}


	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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




}
