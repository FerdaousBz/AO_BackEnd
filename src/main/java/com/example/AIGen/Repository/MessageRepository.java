package com.example.AIGen.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.AIGen.models.Message;

public interface MessageRepository extends MongoRepository<Message, String> {
}
