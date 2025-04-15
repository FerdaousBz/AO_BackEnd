package com.example.AIGen.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.AIGen.Exception.AuthenticationFailedException;
import com.example.AIGen.Repository.UserRepository;
import com.example.AIGen.models.User;
import com.example.AIGen.payload.request.LoginRequest;
import com.example.AIGen.payload.request.OtpRequest;
import com.example.AIGen.payload.response.JwtResponse;
import com.example.AIGen.security.jwt.JwtUtils;

@Service
public class AuthenticationADService {
	

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private OtpService otpService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    UserRepository userRepository;
    
    public JwtResponse authenticateUserAD(LoginRequest loginRequest) throws Exception {
        // Step 1: Authenticate with LDAP
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(), loginRequest.getPassword()
            )
        );
		return null;

       
    }

    public JwtResponse verifyOtp(OtpRequest otpRequest) throws AuthenticationFailedException {
        if (!otpService.verifyOtp(otpRequest.getEmail(), otpRequest.getOtp())) {
            throw new AuthenticationFailedException("Invalid OTP", "FAILED");
        }

        User user = userRepository.findByEmail(otpRequest.getEmail()).orElseThrow(() -> 
            new UsernameNotFoundException("User Not Found with email: " + otpRequest.getEmail()));

        // Generate JWT Token after successful OTP validation
        String jwt = jwtUtils.generateJwtToken(user.getEmail());

        return new JwtResponse(jwt, 
        		user.getId(),
        		user.getUsername(),
        		user.getEmail(),
        		user.getLevel(),
        		user.getProfileImageUrl());
    }
    
}
