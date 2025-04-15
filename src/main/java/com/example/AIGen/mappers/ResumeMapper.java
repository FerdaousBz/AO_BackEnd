package com.example.AIGen.mappers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.example.AIGen.Dto.ParticipantDTO;
import com.example.AIGen.Dto.ProjectDTO;
import com.example.AIGen.Dto.ResumesDTO;
import com.example.AIGen.models.Participants;
import com.example.AIGen.models.Project;
import com.example.AIGen.models.Resumes;

@Component
public class ResumeMapper {
	
//
//	public ResumesDTO toDto (Resumes resume) {
//		
//		ResumesDTO resumesDTO = new ResumesDTO();
//		resumesDTO.setResumeId(resume.getResumeId());
//		resumesDTO.setTitle(resume.getTitle());
//		resumesDTO.setDescription(resume.getDescription());
//		resumesDTO.setSpecifications(resume.getSpecifications());
//		resumesDTO.setDateGenResume(resume.getDateGenResume());
//		resumesDTO.setVersion(resume.getVersion());
//		resumesDTO.setParticipants(toParticipantDtoList(resume.getParticipants()));
//		resumesDTO.setProject(toDto(resume.getProject()));
//		return resumesDTO;
//	}
//
//    public Resumes toEntity(ResumesDTO resumeDTO) {
//        Resumes resume = new Resumes();
//        resume.setResumeId(resumeDTO.getResumeId());
//        resume.setTitle(resumeDTO.getTitle());
//        resume.setDescription(resumeDTO.getDescription());
//        resume.setSpecifications(resumeDTO.getSpecifications());
//        resume.setDateGenResume(resumeDTO.getDateGenResume());
//        resume.setVersion(resumeDTO.getVersion());
//        resume.setParticipants(toParticipantEntityList(resumeDTO.getParticipants()));
//        resume.setProject(toEntity(resumeDTO.getProject()));
//        return resume;
//    }
//    
//    private List<ParticipantDTO> toParticipantDtoList(List<Participants> participants) {
//        return participants.stream().map(this::toDto).collect(Collectors.toList());
//    }
//
//    private List<Participants> toParticipantEntityList(List<ParticipantDTO> participantDTOs) {
//        return participantDTOs.stream().map(this::toEntity).collect(Collectors.toList());
//    }
//    private ParticipantDTO toDto(Participants participant) {
//        ParticipantDTO participantDTO = new ParticipantDTO();
//        participantDTO.setParticipantId(participant.getParticipantId());
//        participantDTO.setParticipantName(participant.getParticipantName());
//        participantDTO.setFonction(participant.getFonction());
//        participantDTO.setResumeId(participant.getResume().getResumeId());
//        return participantDTO;
//    }
//
//    private Participants toEntity(ParticipantDTO participantDTO) {
//        Participants participant = new Participants();
//        participant.setParticipantId(participantDTO.getParticipantId());
//        participant.setParticipantName(participantDTO.getParticipantName());
//        participant.setFonction(participantDTO.getFonction());
//        participant.setResume(new Resumes());
//        participant.getResume().setResumeId(participantDTO.getResumeId());
//        return participant;
//    }
//    
//    private ProjectDTO toDto(Project project) {
//        ProjectDTO projectDTO = new ProjectDTO();
//        projectDTO.setProjectId(project.getProjectId());
//        projectDTO.setProjectName(project.getProjectName());
//        projectDTO.setSecteur(project.getSecteur());
//        projectDTO.setDomain(project.getDomain());
//        projectDTO.setDeadline(project.getDeadline());
//        projectDTO.setDateDebut(project.getDateDebut());
//        projectDTO.setDateFin(project.getDateFin());
//        projectDTO.setResumeId(project.getResume().getResumeId());
//        return projectDTO;
//    }
//
//    private Project toEntity(ProjectDTO projectDTO) {
//        Project project = new Project();
//        project.setProjectId(projectDTO.getProjectId());
//        project.setProjectName(projectDTO.getProjectName());
//        project.setSecteur(projectDTO.getSecteur());
//        project.setDomain(projectDTO.getDomain());
//        project.setDeadline(projectDTO.getDeadline());
//        project.setDateDebut(projectDTO.getDateDebut());
//        project.setDateFin(projectDTO.getDateFin());
//        project.setResume(new Resumes());
//        project.getResume().setResumeId(projectDTO.getResumeId());
//        return project;
//    }
}
