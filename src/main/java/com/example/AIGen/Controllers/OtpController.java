package com.example.AIGen.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.AIGen.models.Otp;
import com.example.AIGen.payload.request.OtpRequest;
import com.example.AIGen.payload.response.ApiResponse;
import com.example.AIGen.payload.response.OtpVerificationResponse;
import com.example.AIGen.services.OtpService;

@RestController
@RequestMapping("/api/otp")
@CrossOrigin
public class OtpController {

	@Autowired
	private OtpService otpService;
	
	 @PostMapping("/verify-otp")
	    public ResponseEntity<ApiResponse> verifyOtp(@RequestBody OtpRequest otpRequest) {
		 boolean isVerified = otpService.verifyOtp(otpRequest.getEmail(), otpRequest.getOtp());
		    if (isVerified) {
		        return ResponseEntity.ok(new ApiResponse("SUCCESS",isVerified,"OTP verified successfully!"));
		    } else {
		        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
		                             .body(new ApiResponse("FAILED",isVerified,"Error: Invalid OTP or OTP expired!"));
		    }
	 }
}
