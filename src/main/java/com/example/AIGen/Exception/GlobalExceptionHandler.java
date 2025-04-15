package com.example.AIGen.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.example.AIGen.payload.response.ApiResponse;
import com.example.AIGen.payload.response.PptGenratedResponse;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGlobalException(Exception ex, WebRequest request) {
        ex.printStackTrace(); // Log the stack trace for debugging
        return new ResponseEntity<>("Internal Server Error: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(PptGenerationException.class)
    public ResponseEntity<PptGenratedResponse> handlePptGenerationException(PptGenerationException ex) {
    	PptGenratedResponse response = new PptGenratedResponse("409", ex.getMessage(), ex.getPptId(), null, null, null, 0);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<PptGenratedResponse> handleRuntimeException(RuntimeException ex) {
    	PptGenratedResponse response = new PptGenratedResponse("500", "Internal server error", null, null, null, null, 0);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
   
    @ExceptionHandler(AuthenticationFailedException.class)
    public ResponseEntity<ApiResponse> handleAuthenticationFailedException(AuthenticationFailedException ex) {
        ApiResponse response = new ApiResponse("FAILED", "[]", "Bad Credentials");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }
    
}
