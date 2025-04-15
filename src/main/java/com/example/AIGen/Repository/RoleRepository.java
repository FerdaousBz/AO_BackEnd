package com.example.AIGen.Repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.AIGen.models.ERole;
import com.example.AIGen.models.Role;

public interface RoleRepository extends MongoRepository<Role, String> {
	  Optional<Role> findByName(ERole name);
	    boolean existsByName(ERole name);
	}