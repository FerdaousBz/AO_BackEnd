package com.example.AIGen.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.AIGen.models.Resumes;

public interface ResumesRepository extends MongoRepository <Resumes, String> {

}
