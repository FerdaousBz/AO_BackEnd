//package com.example.AIGen.mappers;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//import org.aspectj.lang.annotation.AfterReturning;
//import org.springframework.stereotype.Component;
//import org.springframework.web.bind.annotation.Mapping;
//
//import com.example.AIGen.Dto.ParticipantDTO;
//import com.example.AIGen.Dto.ProjectDTO;
//import com.example.AIGen.Dto.UploadFileDTO;
//import com.example.AIGen.models.Participants;
//import com.example.AIGen.models.Project;
//import com.example.AIGen.models.Resumes;
//import com.example.AIGen.models.UploadFile;
//
//@Component
//public class UploadFileMapper {
//	
//	public UploadFileDTO toDto (UploadFile uploadFile) {
//		
//		UploadFileDTO uploadDTO = new UploadFileDTO();
//		uploadDTO.setFileId(uploadFile.getFileId());
//		uploadDTO.setTitle(uploadFile.getTitle());
//		uploadDTO.setDescription(uploadFile.getDescription());
//		uploadDTO.setDateGenResume(uploadFile.getDateGenResume());
////		uploadDTO.setParticipantsDTO(toParticipantDtoList(uploadFile.getParticipants()));
//		if(uploadFile.getProjectId() != null) {
//			uploadDTO.setProjectId(uploadFile.getProjectId());
//		}
//		return uploadDTO;
//	}
//	public UploadFile toEntity (UploadFileDTO uploadDTO) {
//		
//		UploadFile uploadFile = new UploadFile();
//		uploadFile.setFileId(uploadDTO.getFileId());
//		uploadFile.setTitle(uploadDTO.getTitle());
//		uploadFile.setDescription(uploadDTO.getDescription());
//		uploadFile.setDateGenResume(uploadDTO.getDateGenResume());
////		uploadFile.setParticipants(toParticipantEntityList(uploadDTO.getParticipantsDTO()));
//
//		if(uploadDTO.getProjectId() != null) {
//            uploadFile.setProjectId(uploadDTO.getProjectId());
//		}
//		return uploadFile;
//	}
//
////	private List<ParticipantDTO> toParticipantDtoList(List<Participants> participants){
////		 return participants.stream().map(this::toDto).collect(Collectors.toList());
////	}
////    private List<Participants> toParticipantEntityList(List<ParticipantDTO> participantDTOs) {
////        return participantDTOs.stream().map(this::toEntity).collect(Collectors.toList());
////    }
//    private ParticipantDTO toDto(Participants participant) {
//        ParticipantDTO participantDTO = new ParticipantDTO();
//        participantDTO.setParticipantId(participant.getParticipantId());
//        participantDTO.setParticipantName(participant.getParticipantName());
//        participantDTO.setFonction(participant.getFonction());
//        participantDTO.setFileId(participant.getUploadFile().getFileId());
//        return participantDTO;
//    }
//    private Participants toEntity(ParticipantDTO participantDTO) {
//        Participants participant = new Participants();
//        participant.setParticipantId(participantDTO.getParticipantId());
//        participant.setParticipantName(participantDTO.getParticipantName());
//        participant.setFonction(participantDTO.getFonction());
//        participant.setUploadFile(new UploadFile());
//        participant.getUploadFile().setFileId(participantDTO.getFileId());
//        return participant;
//    }
//    
//    public ProjectDTO toDto(Project project) {
//        ProjectDTO projectDTO = new ProjectDTO();
//        projectDTO.setProjectId(project.getProjectId());
//        projectDTO.setProjectName(project.getProjectName());
//        projectDTO.setSecteur(project.getSecteur());
//        projectDTO.setDomain(project.getDomain());
//        projectDTO.setDeadline(project.getDeadline());
//        projectDTO.setDateDebut(project.getDateDebut());
//        projectDTO.setDateFin(project.getDateFin());
//        
//        // Map only the fileId if UploadFile is present
//        if (project.getUploadFile() != null && project.getUploadFile().getFileId() != null) {
//            projectDTO.setFileId(project.getUploadFile().getFileId());
//        }
//        return projectDTO;
//    
//    }
//    public Project toEntity(ProjectDTO projectDTO) {
//        Project project = new Project();
//        project.setProjectId(projectDTO.getProjectId());
//        project.setProjectName(projectDTO.getProjectName());
//        project.setSecteur(projectDTO.getSecteur());
//        project.setDomain(projectDTO.getDomain());
//        project.setDeadline(projectDTO.getDeadline());
//        project.setDateDebut(projectDTO.getDateDebut());
//        project.setDateFin(projectDTO.getDateFin());
//
//        // If fileId is present, set the associated UploadFile
//        if (projectDTO.getFileId() != null) {
//            UploadFile uploadFile = new UploadFile();
//            uploadFile.setFileId(projectDTO.getFileId());
//            project.setUploadFile(uploadFile);
//        }
//        return project;
//    }
//
//    
//}
//
