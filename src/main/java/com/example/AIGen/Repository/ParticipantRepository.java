package com.example.AIGen.Repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.example.AIGen.models.Participants;

public interface ParticipantRepository extends MongoRepository<Participants, String>{
	
	@Query(value="select * from participants")
	List<Participants> getAllParticip();
    List<Participants> findAll();
}
