package com.example.AIGen.services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.example.AIGen.Repository.OtpRepository;
import com.example.AIGen.Repository.UserRepository;
import com.example.AIGen.models.Otp;
import com.example.AIGen.models.User;
import com.example.AIGen.payload.request.OtpRequest;
import com.example.service.impl.OtpServiceImpl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

@Service
public class OtpService implements OtpServiceImpl {

	 @Autowired
	    private JavaMailSender mailSender;

	    @Autowired
	    private UserRepository userRepository;

	    @Autowired
	    private OtpRepository otpRepository; // Inject OTP Repository

//	    private final Map<String, OtpDetails> otpCache = new HashMap<>();
	    private final Set<String> verifiedEmails = new HashSet<>();

	    @Override
	    public void generateOtp(OtpRequest otpRequest) {
	        // Check if email is already registered
	        if (userRepository.existsByEmail(otpRequest.getEmail())) {
	            System.out.println("User with email " + otpRequest.getEmail() + " already has an account. Skipping OTP generation.");
	            return;
	        }

	        otpRepository.deleteByEmail(otpRequest.getEmail()); // Clear any existing OTP for this email

	        String otp = String.format("%05d", new Random().nextInt(100000));
	        LocalDateTime expirationTime = LocalDateTime.now().plusSeconds(300); // 5 minutes

	        Otp otpEntity = new Otp();
	        otpEntity.setEmail(otpRequest.getEmail());
	        otpEntity.setOtp(otp);
	        otpEntity.setExpirationTime(expirationTime);

	        // Save user details temporarily in the OTP entity
	        otpEntity.setLevel(otpRequest.getLevel());
	        otpEntity.setFirstName(otpRequest.getFirstName());
	        otpEntity.setLastName(otpRequest.getLastName());
	        otpEntity.setResourceId(otpRequest.getResourceId());
	        otpRepository.save(otpEntity); // Save OTP and user details in the database

	        sendOtpEmail(otpRequest.getEmail(), otp); // Send OTP email
	    }

	    private void sendOtpEmail(String email, String otp) {
	        try {
	            SimpleMailMessage message = new SimpleMailMessage();
	            message.setTo(email);
	            message.setSubject("Reset Password OTP Code");
	            message.setText("Your OTP code is: " + otp);
	            System.out.println("Sending email to: " + email);
	            mailSender.send(message);
	            System.out.println("OTP email sent successfully to " + email);
	        } catch (Exception e) {
	            System.err.println("Error sending email to " + email + ": " + e.getMessage());
	            e.printStackTrace();
	            throw new RuntimeException("Could not send email", e);
	        }
	    }

	    @Override
	    public boolean verifyOtp(String email, String otp) {
	    	try {
	            Optional<Otp> otpEntityOpt = otpRepository.findByEmail(email);

	            if (otpEntityOpt.isPresent()) {
	                Otp otpEntity = otpEntityOpt.get();
	                if (otpEntity.getOtp().equals(otp) && LocalDateTime.now().isBefore(otpEntity.getExpirationTime())) {
	                    
	                	// OTP is valid, create user with stored details and delete the OTP
	                    User user = new User();
	                    user.setEmail(email);

	                    // Concatenate firstName and lastName to create username
	                    String username = otpEntity.getFirstName() + " " + otpEntity.getLastName();
	                    user.setUsername(username); // Set the concatenated username
	                    user.setLevel(otpEntity.getLevel());
	                    user.setOtpVerified(true); // Set verified status
	                    user.setBoondManagerId(otpEntity.getResourceId());

	                    userRepository.save(user); // Save user to the database
	                    otpRepository.delete(otpEntity); // Delete OTP after successful verification

	                    return true;
	                }
	            }
	            
	        } catch (IncorrectResultSizeDataAccessException e) {
	            otpRepository.deleteByEmail(email); // Clear duplicate OTP entries
	            throw new RuntimeException("Duplicate OTP entries found for email. Please request a new OTP.");
	        }
	        return false;
	    }

	    public boolean isOtpVerified(String email) {
	        return verifiedEmails.contains(email);
	    }

	    public void removeVerifiedOtp(String email) {
	        verifiedEmails.remove(email);
	    }

	
	    //Generate OTP for forget password
	    @Override
	    public void generateOtpForPasswordReset(String email) {
	        otpRepository.deleteByEmail(email); 

	        String otp = String.format("%05d", new Random().nextInt(100000));
	        LocalDateTime expirationTime = LocalDateTime.now().plusSeconds(300); // 5 minutes

	        Otp otpEntity = new Otp();
	        otpEntity.setEmail(email);
	        otpEntity.setOtp(otp);
	        otpEntity.setExpirationTime(expirationTime);
	        
	        otpRepository.save(otpEntity); // Save OTP in the database

	        sendOtpEmail(email, otp); 
	    }

	    @Override
	    public boolean verifyPasswordResetOtp(String email, String otp) {
	        Optional<Otp> otpEntityOpt = otpRepository.findByEmail(email);
	        
	        if (otpEntityOpt.isPresent()) {
	            Otp otpEntity = otpEntityOpt.get();
	            if (otpEntity.getOtp().equals(otp) && LocalDateTime.now().isBefore(otpEntity.getExpirationTime())) {
	                otpRepository.delete(otpEntity); // Delete OTP after successful verification
	                return true;
	            }
	        }
	        return false;
	    }
		
	}



