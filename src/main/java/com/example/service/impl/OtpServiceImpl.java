package com.example.service.impl;

import com.example.AIGen.models.Otp;
import com.example.AIGen.payload.request.OtpRequest;

public interface OtpServiceImpl {


    boolean verifyOtp(String email, String otp);
    boolean isOtpVerified(String email);
	void removeVerifiedOtp(String email);
	void generateOtp(OtpRequest otpRequest);
	public void generateOtpForPasswordReset(String email);
	public boolean verifyPasswordResetOtp(String email, String otp);
}
