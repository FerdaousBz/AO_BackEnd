package com.example.AIGen.Repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.AIGen.models.UploadFile;

public interface UploadFileRepository  extends MongoRepository <UploadFile, String>{
	
	Optional<UploadFile> findByFileId(String fileId);
	boolean existsByFileId(String fileId);

}
