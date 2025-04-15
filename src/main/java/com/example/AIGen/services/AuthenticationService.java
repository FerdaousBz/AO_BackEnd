package com.example.AIGen.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.AIGen.Exception.AuthenticationFailedException;
import com.example.AIGen.Repository.UserRepository;
import com.example.AIGen.models.User;
import com.example.AIGen.payload.request.EmailRequest;
import com.example.AIGen.payload.request.LoginRequest;
import com.example.AIGen.payload.request.OtpRequest;
import com.example.AIGen.payload.request.PasswordRequest;
import com.example.AIGen.payload.response.ApiResponse;
import com.example.AIGen.payload.response.JwtResponse;
import com.example.AIGen.security.jwt.JwtUtils;
import com.example.service.impl.AuthenticationServiceImpl;
import com.fasterxml.jackson.databind.JsonNode;

import jakarta.servlet.http.HttpSession;

@Service
public class AuthenticationService implements AuthenticationServiceImpl {

    @Autowired
    AuthenticationManager authenticationManager;
    
    @Autowired
    JwtUtils jwtUtils;
    
    @Autowired
    PasswordEncoder encoder;
    
    @Autowired
    UserRepository userRepository;
    
	@Autowired
	private BoondManagerAPIClient apiClient;
	
    @Autowired
    OtpService otpService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    public JwtResponse authenticateUser(LoginRequest loginRequest) throws Exception { 
    	 // Check if user exists
        Optional<User> user = userRepository.findByEmail(loginRequest.getEmail());
        if (user.isEmpty()) {
            throw new AuthenticationFailedException("User not found", "USER_NOT_FOUND");
        }

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(), 
                            loginRequest.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            return new JwtResponse(
                jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                userDetails.getLevel(),
                userDetails.getProfileImageUrl()
            );
            
        } catch (BadCredentialsException e) {
        	
            throw new AuthenticationFailedException("Bad credentials", "FAILED");
            
        } catch (Exception e) {
        	
            throw new Exception("Authentication error", e);
        }
    }
    
    
    @Override
    public ResponseEntity<?> checkEmailAndSendOtp(EmailRequest emailRequest){
        String email = emailRequest.getEmail();
        String level ;
        String firstName = null ;
        String lastName = null;
        String resourceId = null;
        // Check if email exists in the database
        if (userRepository.existsByEmail(email)) {
            return ResponseEntity.badRequest().body(new ApiResponse("FAILED", "[]", "Error: Email already exists"));
        }

        // Check if email ends with @jems-group.com
        if (!email.endsWith("@jems-group.com")) {
            return ResponseEntity.badRequest().body(new ApiResponse("FAILED", "[]", "Error: Email not valid"));
        }

        // Call BoondManager API to retrieve resources
        JsonNode resourcesResponse = apiClient.getFromBoondManagerAPI("/resources", 1, 100);

        // Find resource ID by email and extract first and last name
        if (resourcesResponse.has("data")) {
            for (JsonNode resource : resourcesResponse.get("data")) {
                String resourceEmail = resource.get("attributes").get("email1").asText();
                if (email.equals(resourceEmail)) {
                    resourceId = resource.get("id").asText();
                    firstName = resource.get("attributes").get("firstName").asText();
                    lastName = resource.get("attributes").get("lastName").asText();
                    break;
                }
            }
        }

        if (resourceId == null) {
            return ResponseEntity.badRequest().body(new ApiResponse("FAILED", "[]", "Error: Email not found in resources."));
        }

        // Retrieve detailed information for the resource
        JsonNode resourceResponse = apiClient.getFromBoondManagerAPI("/resources/" + resourceId, 1, 100);

        if (resourceResponse.has("data") && resourceResponse.get("data").has("attributes")) {
            level = resourceResponse.get("data").get("attributes").get("level").asText();

            // Check if the level is not a "manager"
            if ("manager".equalsIgnoreCase(level)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ApiResponse("FAILED", "[]", "Error: Unauthorized role level. User level cannot be 'resource'."));
            }
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("FAILED", "[]", "Error: Unable to retrieve user role from BoondManager."));
        }

        OtpRequest otpRequest = new OtpRequest(email, level, firstName, lastName,resourceId);
        // Generate OTP
        otpService.generateOtp(otpRequest);

        // Prepare success response with user details
        ApiResponse.Data data = new ApiResponse.Data(email, level, firstName, lastName,resourceId);
        ApiResponse response = new ApiResponse("SUCCESS", data, "OTP sent to email successfully. Please verify to complete registration.");
        return ResponseEntity.ok(response);
    }
    
    @Override
    public ResponseEntity<?> createPassword(PasswordRequest passwordRequest) {
    	 Optional<User> userOpt = userRepository.findByEmail(passwordRequest.getEmail());

         if (userOpt.isEmpty()) {
             return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                  .body(new ApiResponse("FAILED", userOpt,"Error: User not found!"));
         }

         User user = userOpt.get();

         // Check if OTP was verified
         if (!user.isOtpVerified()) {
             return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                  .body(new ApiResponse("FAILED",user, "Error: OTP not verified for this email!"));
         }

         // Check if passwords match
         if (!passwordRequest.getPassword().equals(passwordRequest.getConfirmPassword())) {
             return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                  .body(new ApiResponse("FAILED",user, "Error: Passwords do not match!"));
         }

         // Set and save new password
         user.setPassword(passwordEncoder.encode(passwordRequest.getPassword()));
         userRepository.save(user);

         return ResponseEntity.ok(new ApiResponse("SUCCESS",user, "Password created successfully!"));
     }
    
    @Override
    public ResponseEntity<?> initiatePasswordReset(String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("FAILED", "[]", "User not found!"));
        }
        
        otpService.generateOtpForPasswordReset(email);
        return ResponseEntity.ok(new ApiResponse("SUCCESS", "[]", "An OTP for password reset has been sent to your email."));
    }
    
    @Override
    public ResponseEntity<?> verifyPasswordResetOtp(String email, String otp) {
        if (otpService.verifyPasswordResetOtp(email, otp)) {
            return ResponseEntity.ok(new ApiResponse("SUCCESS", true, "OTP verified successfully. You may now reset your password."));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("FAILED", false, "Invalid or expired OTP."));
        }
    }
    
    @Override
    public ResponseEntity<?> resetPassword(PasswordRequest passwordRequest) {
        Optional<User> userOpt = userRepository.findByEmail(passwordRequest.getEmail());

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("FAILED", true, "User not found!"));
        }

        User user = userOpt.get();
        
        // Check if passwords match
        if (!passwordRequest.getPassword().equals(passwordRequest.getConfirmPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("FAILED", false, "Passwords do not match!"));
        }

        // Set and save new password
        user.setPassword(passwordEncoder.encode(passwordRequest.getPassword()));
        userRepository.save(user);

        return ResponseEntity.ok(new ApiResponse("SUCCESS", true, "Password reset successfully!"));
    }
    
}
