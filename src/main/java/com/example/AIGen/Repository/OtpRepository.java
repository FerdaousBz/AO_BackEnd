package com.example.AIGen.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.AIGen.models.Otp;


public interface OtpRepository  extends MongoRepository<Otp, String>{

	Optional<Otp> findByEmail(String email);
    void deleteByEmail(String email);
}
