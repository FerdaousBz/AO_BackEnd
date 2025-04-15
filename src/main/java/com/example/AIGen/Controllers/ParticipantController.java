package com.example.AIGen.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.AIGen.models.Participants;
import com.example.AIGen.services.ParticipantService;

@RestController
@RequestMapping("/api/participant")
public class ParticipantController {

    @Autowired
    private ParticipantService participantService;
	
    @PostMapping("/addparticipant")
    public ResponseEntity<Participants> createParticipant(@RequestBody Participants participant) {
        Participants savedParticipant = participantService.saveParticipant(participant);
        return ResponseEntity.ok(savedParticipant);
    }

    @GetMapping("/{participantId}")
    public ResponseEntity<Participants> getParticipantById(@PathVariable String participantId) {
        Participants participant = participantService.getParticipantById(participantId);
        return ResponseEntity.ok(participant);
    }

    @GetMapping("/allparticipant")
    public ResponseEntity<List<Participants>> getAllParticipants() {
        List<Participants> participants = participantService.getAllParticipants();
        return ResponseEntity.ok(participants);
    }
    
}
