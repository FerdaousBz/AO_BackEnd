package com.example.service.impl;

import org.springframework.http.ResponseEntity;

import com.example.AIGen.payload.request.EmailRequest;
import com.example.AIGen.payload.request.LoginRequest;
import com.example.AIGen.payload.request.PasswordRequest;
import com.example.AIGen.payload.response.JwtResponse;

import jakarta.servlet.http.HttpSession;

public interface AuthenticationServiceImpl {

	//Signin Service
	JwtResponse authenticateUser (LoginRequest loginRequest) throws Exception;
	
	//CheckEmail Service
	ResponseEntity<?> checkEmailAndSendOtp (EmailRequest emailRequest);
		
	//Create password<Inscription>
	ResponseEntity<?> createPassword(PasswordRequest passwordRequest);

	ResponseEntity<?> initiatePasswordReset(String email);

	ResponseEntity<?> verifyPasswordResetOtp(String email, String otp);

	ResponseEntity<?> resetPassword(PasswordRequest passwordRequest);
}
