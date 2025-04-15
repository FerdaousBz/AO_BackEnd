package com.example.AIGen.Repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.AIGen.models.Conversation;

public interface ConversationRepository extends MongoRepository<Conversation, String> {
	List<Conversation> findByUserId(String userId);
}
