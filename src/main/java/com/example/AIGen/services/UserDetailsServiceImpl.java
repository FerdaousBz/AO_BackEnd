package com.example.AIGen.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.AIGen.Repository.UserRepository;
import com.example.AIGen.models.User;
import com.example.AIGen.payload.request.PasswordRequest;
import com.example.AIGen.payload.request.PasswordUpdateRequest;
import com.example.AIGen.payload.request.UpdateUserRequest;
import com.example.AIGen.payload.response.ApiResponse;
import com.example.AIGen.security.jwt.JwtUtils;
import com.fasterxml.jackson.databind.JsonNode;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	
	@Autowired
	UserRepository userRepository;
	
   @Autowired
    private PasswordEncoder passwordEncoder;
	
   @Autowired
   private JavaMailSender mailSender;

   @Autowired
   private JwtUtils jwtUtils;

   @Autowired
   private Environment env;
   
   
   @Value("${bezkoder.openapi.dev-url}")
   private String Devurl;
   
   @Autowired 
   private BoondManagerAPIClient apiClient;
   
	@Autowired
	private DictionaryService dictionaryService;
   @Autowired
   private CloudinaryService cloudinaryService;
   
	@Override
	@Transactional
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + email));

		return UserDetailsImpl.build(user);
	}
	
	 // Initiate email to send link reset password
    public boolean initiatePasswordReset(String email) {
        try {
            Optional<User> optionalUser = userRepository.findByEmail(email);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                String token = jwtUtils.generateJwtToken(email);
                user.setResetToken(token);
                userRepository.save(user);
                sendResetPasswordEmail(email, token);
                return true;
            } else {
                System.out.println("User not found for email: " + email);
                return false;
            }
        } catch (Exception e) {
            System.err.println("Error initiating password reset for email: " + email);
            e.printStackTrace();
            throw new RuntimeException("Failed to initiate password reset", e);
        }
    }

    // Reset password for forgot password
    public boolean updatePassword(String token, String newPassword) {
        String email = jwtUtils.getEmailFromJwtToken(token);
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setPassword(passwordEncoder.encode(newPassword));
            user.setResetToken(null); // Clear the reset token after password update
            userRepository.save(user);
            return true;
        }
        return false;
       
    }
    public boolean updatePass(String token, String currentPassword, String newPassword) {
        // Extract email from the JWT token
        String email = jwtUtils.getEmailFromJwtToken(token);

        // Fetch the user by email
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            // Check if the new password is null or empty
            if (newPassword == null || newPassword.trim().isEmpty()) {
                throw new RuntimeException("Error: New password must not be empty.");
            }

            // Check if the new password is different from the current password
            if (passwordEncoder.matches(newPassword, user.getPassword())) {
                throw new RuntimeException("Error: New password must be different from the current password.");
            }

            // Check if the current password matches the stored password
            if (passwordEncoder.matches(currentPassword, user.getPassword())) {
                // Update the password with the new password
                user.setPassword(passwordEncoder.encode(newPassword));
                userRepository.save(user); // Save the updated user
                return true; // Password updated successfully
            } else {
                throw new RuntimeException("Error: Current password is incorrect.");
            }
        }
        throw new RuntimeException("Error: User not found.");
    }

    // Preparing email to send it for the reset password
    private void sendResetPasswordEmail(String email, String token) {
    	  try {
    		  String environment = env.getProperty("app.environment");
    	        System.out.println("Current environment: " + environment);
    	        
    	        String resetUrl;

    	        // Check for the correct environment
    	        if ("production".equalsIgnoreCase(environment)) {
    	            // Use the development URL for local environment
    	            resetUrl = env.getProperty("bezkoder.openapi.dev-url") + "/reset-password";
    	            System.out.println("Development URL: " + resetUrl);
    	        } else {
    	            // Use the production URL for all other environments (e.g., production)
    	            resetUrl = env.getProperty("bezkoder.openapi.prod-url") + "/reset-password";
    	            System.out.println("Production URL: " + resetUrl);
    	        }

    	        resetUrl += "?token=" + token;
    	        System.out.println("Reset URL: " + resetUrl); // Log the complete URL

    	        SimpleMailMessage message = new SimpleMailMessage();
    	        message.setTo(email);
    	        message.setSubject("Reset Your Password");
    	        message.setText("Please click the link below to reset your password:\n" + resetUrl);
    	        mailSender.send(message);

    	        System.out.println("Reset password email sent successfully to: " + email);
    	    } catch (Exception e) {
    	        System.err.println("Error sending reset password email to " + email + ": " + e.getMessage());
    	        e.printStackTrace();
    	        throw new RuntimeException("Could not send email", e);
    	    }
    }

    //get UserDetails Information from boondmanager
    public ResponseEntity<ApiResponse> getUserDetailsByEmail(String email) {
    	try {
            // Fetch the relevant user details from MongoDB
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Get the BoondManager resource ID from the user object
            String resourceId = user.getBoondManagerId();

            // Fetch the resource details from BoondManager API
            JsonNode resourceResponse = apiClient.getRessourceFromBoondManagerAPI("/resources/" + resourceId + "/information", 1, 100);

            // Extract the "data" node and the "attributes" node from the API response
            JsonNode dataNode = resourceResponse.at("/data");
            JsonNode attributes = dataNode.at("/attributes");

            // Prepare the Map to combine both user data (from MongoDB) and BoondManager resource data
            Map<String, Object> combinedUserData = new HashMap<>();

            // Add user-related data (from MongoDB)
            combinedUserData.put("email", user.getEmail());
            combinedUserData.put("username", user.getUsername());
            combinedUserData.put("password", user.getPassword());
            combinedUserData.put("level", user.getLevel());

            // Add BoondManager resource data (from API response)
            combinedUserData.put("resourceId", dataNode.at("/id").asText());
            combinedUserData.put("type", dataNode.at("/type").asText());
            combinedUserData.put("title", attributes.path("title").asText());
            combinedUserData.put("creationDate", attributes.path("creationDate").asText());
            combinedUserData.put("updateDate", attributes.path("updateDate").asText());
            combinedUserData.put("civility", attributes.path("civility").asInt());
            combinedUserData.put("dateOfBirth", attributes.path("dateOfBirth").asText());
            combinedUserData.put("numberOfResumes", attributes.path("numberOfResumes").asInt());
            

            // Extract thumbnail
            String thumbnail = attributes.path("thumbnail").asText(null);
            if (thumbnail != null) {
                try {
                    byte[] thumbnailData = apiClient.fetchThumbnail(thumbnail);
                    combinedUserData.put("thumbnail", thumbnailData);
                } catch (RuntimeException e) {
                    combinedUserData.put("thumbnail", thumbnail); // Handle errors gracefully
                }
            }

            // Extract relationships from "included" and prepare the structured list
            JsonNode includedArray = resourceResponse.path("included");
            List<Map<String, Object>> includedList = new ArrayList<>();

            if (includedArray.isArray()) {
                for (JsonNode includedNode : includedArray) {
                    Map<String, Object> includedData = new HashMap<>();
                    includedData.put("id", includedNode.path("id").asText());
                    includedData.put("type", includedNode.path("type").asText());

                    // Extract attributes for each included item
                    JsonNode includedAttributes = includedNode.path("attributes");
                    Map<String, Object> attributesMap = new HashMap<>();
                    includedAttributes.fields().forEachRemaining(field -> {
                        attributesMap.put(field.getKey(), field.getValue().asText());
                    });
                    includedData.put("attributes", attributesMap);

                    includedList.add(includedData);
                }
            }

            // Add the "included" list to the combined data
            combinedUserData.put("included", includedList);
            // Return the combined data in the ApiResponse format
            ApiResponse apiResponse = new ApiResponse("success", combinedUserData, "User data fetched successfully");
            return ResponseEntity.ok(apiResponse);

        } catch (NoSuchElementException e) {
            ApiResponse apiResponse = new ApiResponse("error", null, "User not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiResponse);
        } catch (Exception e) {
            ApiResponse apiResponse = new ApiResponse("error", null, "Error processing user and BoondManager data: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);
        }
   }
    	
    
    
//    public User updateUserDetails(String email, UpdateUserRequest userRequest, byte[] profileImageData) {
//    	
//    	 Optional<User> userOptional = userRepository.findByEmail(email);
//    	    if (userOptional.isPresent()) {
//    	        User user = userOptional.get();
//    	        user.setUsername(userRequest.getUsername());
//
//    	        if (userRequest.getPassword() != null && !userRequest.getPassword().isEmpty()) {
//    	            user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
//    	        }
//
//    	        // Handle profile image update
//    	        if (profileImageData != null) {
//    	            String publicId = "profile_images/" + user.getId(); // Cloudinary path
//    	            String uploadedImageUrl = null;
//					try {
//						uploadedImageUrl = cloudinaryService.uploadImage(profileImageData, publicId);
//					} catch (IOException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//    	            user.setProfileImageUrl(uploadedImageUrl); // Update the image URL in the user object
//    	        }
//
//    	        return userRepository.save(user);
//    	    }
//    	    return null;
//    }
    
    
    
}
