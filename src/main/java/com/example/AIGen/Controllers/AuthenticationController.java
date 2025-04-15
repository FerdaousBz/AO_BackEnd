package com.example.AIGen.Controllers;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.AIGen.Repository.RoleRepository;
import com.example.AIGen.Repository.UserRepository;
import com.example.AIGen.models.ERole;
import com.example.AIGen.models.Role;
import com.example.AIGen.models.User;
import com.example.AIGen.payload.request.EmailRequest;
import com.example.AIGen.payload.request.LoginRequest;
import com.example.AIGen.payload.request.OtpRequest;
import com.example.AIGen.payload.request.PasswordRequest;
import com.example.AIGen.payload.request.PasswordUpdateRequest;
import com.example.AIGen.payload.request.SignUpRequest;
import com.example.AIGen.payload.response.ApiResponse;
import com.example.AIGen.payload.response.JwtResponse;
import com.example.AIGen.payload.response.MessageResponse;
import com.example.AIGen.security.jwt.JwtUtils;
import com.example.AIGen.services.AuthenticationService;
import com.example.AIGen.services.BoondManagerAPIClient;
import com.example.AIGen.services.OtpService;
import com.example.AIGen.services.UserDetailsImpl;
import com.example.AIGen.services.UserDetailsServiceImpl;
import com.fasterxml.jackson.databind.JsonNode;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthenticationController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    OtpService otpService;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    UserDetailsServiceImpl userDetailsServiceImpl;
    
    @Autowired
    BoondManagerAPIClient apiClient;

    @Autowired
    AuthenticationService authenticationService;
    
    

   @PostMapping("/signin")
   public ResponseEntity<ApiResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) throws Exception {
      
           JwtResponse jwtResponse = authenticationService.authenticateUser(loginRequest);
           
           ApiResponse response= new ApiResponse("SUCCESS",jwtResponse, "Login successfully !");
           return ResponseEntity.ok(response);

   }
    @PostMapping("/verify-email")
    public ResponseEntity<?> checkEmailSendOtp(@RequestBody EmailRequest emailRequest){
    	   ResponseEntity<?> response = authenticationService.checkEmailAndSendOtp(emailRequest);
    	        	    ApiResponse apiResponse = (ApiResponse) response.getBody();
    	  
    	    if (response.getStatusCode().is2xxSuccessful()) {
    	        return ResponseEntity.ok(new ApiResponse("SUCCESS", apiResponse.getData(), "Otp Sent Successfully."));
    	    } else {
    	        return ResponseEntity.status(response.getStatusCode()).body(
    	            new ApiResponse("FAILED", "[]", apiResponse.getMessage())
    	        );
    	    }
    }
    
    @PostMapping("/signup")
    public ResponseEntity<?> createPassword(@Valid @RequestBody PasswordRequest passwordRequest) {
        return authenticationService.createPassword(passwordRequest);
    }
    
    @PostMapping("/forgot-password")
    public ResponseEntity<?> initiatePasswordReset(@RequestBody EmailRequest emailRequest) {
        return authenticationService.initiatePasswordReset(emailRequest.getEmail());
    }

    @PostMapping("/verify-reset-otp")
    public ResponseEntity<?> verifyPasswordResetOtp(@RequestBody OtpRequest otpRequest) {
        return authenticationService.verifyPasswordResetOtp(otpRequest.getEmail(), otpRequest.getOtp());
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody PasswordRequest passwordRequest) {
        return authenticationService.resetPassword(passwordRequest);
    }  
    

//  //Register new User :  
//    @PostMapping("/register")
//    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signupRequest) {
//        // Check if user exists by email
//        Optional<User> existingUserOptional = userRepository.findByEmail(signupRequest.getEmail());
//
//        Set<Role> roles = new HashSet<>();
//
//        // If the user exists, update their roles
//        if (existingUserOptional.isPresent()) {
//            User existingUser = existingUserOptional.get();
//            
//            // Add roles based on the request
//            Set<String> strRoles = signupRequest.getRoles();
//            if (strRoles == null) {
//                // Default role if no roles are provided
//                Role userRole = roleRepository.findByName(ERole.ROLE_USER)
//                        .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
//                roles.add(userRole);
//            } else {
//                strRoles.forEach(role -> {
//                    switch (role) {
//                        case "admin":
//                            Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
//                                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
//                            roles.add(adminRole);
//                            break;
//                        case "vp":
//                            Role vpRole = roleRepository.findByName(ERole.ROLE_VP)
//                                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
//                            roles.add(vpRole);
//                            break;
//                        default:
//                            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
//                                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
//                            roles.add(userRole);
//                    }
//                });
//            }
//            
//            // Assign roles to the existing user
//            existingUser.setRoles(roles);
//            userRepository.save(existingUser);
//            
//            // Return JSON response for existing user
//            return ResponseEntity.ok(Map.of("message", "User roles updated successfully!", "user", existingUser));
//        } else {
//            // Create new user account if not existing
//            User newUser = new User(signupRequest.getUsername(),
//                                     signupRequest.getEmail(),
//                                     encoder.encode(signupRequest.getPassword()));
//            
//            // Set roles for the new user
//            Set<String> strRoles = signupRequest.getRoles();
//            if (strRoles == null) {
//                // Default role if no roles are provided
//                Role userRole = roleRepository.findByName(ERole.ROLE_USER)
//                        .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
//                roles.add(userRole);
//            } else {
//                strRoles.forEach(role -> {
//                    switch (role) {
//                        case "admin":
//                            Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
//                                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
//                            roles.add(adminRole);
//                            break;
//                        case "vp":
//                            Role vpRole = roleRepository.findByName(ERole.ROLE_VP)
//                                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
//                            roles.add(vpRole);
//                            break;
//                        default:
//                            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
//                                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
//                            roles.add(userRole);
//                    }
//                });
//            }
//
//            newUser.setRoles(roles);
//            userRepository.save(newUser);
//
//            // Return JSON response for new user
//            return ResponseEntity.ok(Map.of("message", "User registered successfully!", "user", newUser));
//        }
//    }
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signupRequest) {
        try {
            // Validate that the password and confirmPassword match
            if (!signupRequest.getPassword().equals(signupRequest.getConfirmPassword())) {
                return ResponseEntity.badRequest().body(Map.of("message", "Error: Passwords do not match!"));
            }

            // Check if the user already exists by email
            Optional<User> existingUserOptional = userRepository.findByEmail(signupRequest.getEmail());
            if (existingUserOptional.isPresent()) {
                return ResponseEntity.badRequest().body(Map.of("message", "Error: User with this email already exists!"));
            }

            // Create a new user account if not existing
            User newUser = new User(
                    signupRequest.getEmail(),
                    encoder.encode(signupRequest.getPassword()) // Encrypt the password
            );
            // Return JSON response for new user
            return ResponseEntity.ok(Map.of("message", "User registered successfully!", "user", newUser));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Error during registration", "errorDetails", e.getMessage()));
        }
    }

}