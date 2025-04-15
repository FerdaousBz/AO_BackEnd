package com.example.AIGen.payload.response;

public class OtpVerificationResponse {

    private String email;
    private boolean isVerified;

    public OtpVerificationResponse(String email, boolean isVerified) {
        this.email = email;
        this.isVerified = isVerified;
    }

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isVerified() {
		return isVerified;
	}

	public void setVerified(boolean isVerified) {
		this.isVerified = isVerified;
	}
    
}
