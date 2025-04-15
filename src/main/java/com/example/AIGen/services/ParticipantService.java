package com.example.AIGen.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.example.AIGen.Repository.ParticipantRepository;
import com.example.AIGen.models.Participants;


@Service
public class ParticipantService {

	   @Autowired
	    private ParticipantRepository participantRepository;

	   @Autowired
	    private MongoTemplate mongoTemplate;
	   
	   
	    public Participants saveParticipant(Participants participant) {
	        return participantRepository.save(participant);
	    }

	    public Participants getParticipantById(String _id) {
	        return participantRepository.findById(_id).orElse(null);
	    }

	    public List<Participants> getAllParticipants() {
	    	return participantRepository.findAll();
//	    	Query query = new Query();
//	        return mongoTemplate.find(query, Participants.class);	
	        }
}
