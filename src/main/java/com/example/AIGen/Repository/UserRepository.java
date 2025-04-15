package com.example.AIGen.Repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.AIGen.models.User;

public interface UserRepository extends MongoRepository<User, String> {
	  Optional<User> findByUsername(String username);
	  Optional<User> findByEmail(String email);
	  Optional<User> findByResetToken(String token);
	  Boolean existsByUsername(String username);
	  
	  @Query("SELECT u FROM User u WHERE u.email = :email")
	  User findEmail(@Param("email") String email);

	  boolean existsByEmail(String email);
	}