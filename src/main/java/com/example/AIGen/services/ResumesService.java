package com.example.AIGen.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.AIGen.Dto.ParticipantDTO;
import com.example.AIGen.Dto.ProjectDTO;
import com.example.AIGen.Dto.ResumesDTO;
import com.example.AIGen.Repository.ParticipantRepository;
import com.example.AIGen.Repository.ProjectRepository;
import com.example.AIGen.Repository.ResumesRepository;
import com.example.AIGen.mappers.ResumeMapper;
import com.example.AIGen.models.Participants;
import com.example.AIGen.models.Project;
import com.example.AIGen.models.Resumes;

@Service
public class ResumesService {

	@Autowired
	private ResumesRepository resumesRepository;
	
	@Autowired
	private ResumeMapper resumeMapper;
	
	@Autowired
	private ParticipantRepository participantsRepository;
	
	@Autowired
	private ProjectRepository projectRepository;
	
    @Autowired
    private EmailService emailService;
	
// public ResumesDTO createResume(ResumesDTO resumeDTO) {
//        Resumes resume = resumeMapper.toEntity(resumeDTO);
//        Resumes savedResume = resumesRepository.save(resume);
//
//        if (savedResume.getParticipants() != null) {
//            for (Participants participant : savedResume.getParticipants()) {
//                participant.setResume(savedResume);
//                participantsRepository.save(participant);
//            }
//        }
//
//        if (savedResume.getProject() != null) {
//            Project project = savedResume.getProject();
//            project.setResume(savedResume);
//            projectRepository.save(project);
//        }
//        // Convert the saved resume back to DTO and send an email
//        ResumesDTO savedResumeDTO = resumeMapper.toDto(savedResume);
//        sendResumeEmail(savedResumeDTO);
//        return savedResumeDTO;
//        //return resumeMapper.toDto(savedResume);
//    }

// private void sendResumeEmail(ResumesDTO resumeDTO) {
//	    String to = "schaieb@jems-group.com"; // Change this to the actual recipient's email
//	    String subject = "Resume: " + resumeDTO.getTitle();
//	    String text = createEmailContent(resumeDTO);
//
//	    emailService.sendResumeEmail(to, subject, text);
//	}
// private String createEmailContent(ResumesDTO resumeDTO) {
//	    StringBuilder content = new StringBuilder();
//	    content.append("Détails du curriculum vitae :\n");
//	    content.append("Titre: ").append(resumeDTO.getTitle()).append("\n");
//	    content.append("Date de génération: ").append(resumeDTO.getDateGenResume()).append("\n");
//	    content.append("Description: ").append(resumeDTO.getDescription()).append("\n");
//	    content.append("Spécifications: ").append(resumeDTO.getSpecifications()).append("\n");
//	    content.append("Version: ").append(resumeDTO.getVersion()).append("\n");
//	    
//	    content.append("\nParticipants:\n");
//	    for (ParticipantDTO participant : resumeDTO.getParticipants()) {
//	        content.append("- Nom: ").append(participant.getParticipantName()).append("\n");
//	        content.append("  Fonction: ").append(participant.getFonction()).append("\n");
//	        content.append("  Email: ").append(participant.getEmail()).append("\n");
//	    }
//
//	    if (resumeDTO.getProject() != null) {
//	        content.append("\nProjet:\n");
//	        content.append("Nom du projet: ").append(resumeDTO.getProject().getProjectName()).append("\n");
//	        content.append("Secteur: ").append(resumeDTO.getProject().getSecteur()).append("\n");
//	        content.append("Domaine: ").append(resumeDTO.getProject().getDomain()).append("\n");
//	        content.append("Date limite: ").append(resumeDTO.getProject().getDeadline()).append("\n");
//	    }
//
//	    return content.toString();
//	}
}
