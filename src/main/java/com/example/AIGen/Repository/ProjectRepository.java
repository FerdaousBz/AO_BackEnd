package com.example.AIGen.Repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.AIGen.models.Project;

public interface ProjectRepository extends MongoRepository<Project, String> {
    Optional<Project> findByProjectId(String projectId);
	
}
