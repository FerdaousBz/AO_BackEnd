package com.example.AIGen.Controllers;

import java.io.IOException;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.AIGen.models.User;
import com.example.AIGen.payload.request.UpdateUserRequest;
import com.example.AIGen.payload.response.ApiResponse;
import com.example.AIGen.payload.response.UserDetailsResponse;
import com.example.AIGen.services.UserDetailsServiceImpl;
import com.fasterxml.jackson.databind.JsonNode;

@RestController
@RequestMapping("/api/resource")
@CrossOrigin
public class UserController {
	
	@Autowired
	private UserDetailsServiceImpl userDetailsServiceImpl;
	
	@GetMapping("/user/{email}")
	public ResponseEntity<ApiResponse> getUserById(@PathVariable String email) {
		return userDetailsServiceImpl.getUserDetailsByEmail(email);
	}

//	@PutMapping("/update/user/{email}")
//	public ResponseEntity<UserDetailsResponse> updateUserDetails(
//			@PathVariable String email,
//			@ModelAttribute UpdateUserRequest userRequest,
//			@RequestPart(value = "profileImage", required = false) MultipartFile profileImage){
//		
//		  try {
//		        byte[] profileImageData = profileImage != null ? profileImage.getBytes() : null;
//
//		        User updatedUser = userDetailsServiceImpl.updateUserDetails(email, userRequest, profileImageData);
//
//		        if (updatedUser != null) {
//		            UserDetailsResponse response = new UserDetailsResponse(
//		                    "Success",
//		                    "User details updated successfully.",
//		                    new UserDetailsResponse.Data(updatedUser)
//		            );
//		            return ResponseEntity.ok(response);
//		        } else {
//		            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//		                    .body(new UserDetailsResponse("Error", "User not found.", null));
//		        }
//		    } catch (IOException e) {
//		        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//		                .body(new UserDetailsResponse("Error", "Failed to process profile image.", null));
//		    } catch (Exception e) {
//		        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//		                .body(new UserDetailsResponse("Error", "Failed to update user details.", null));
//		    }
//	}
}
