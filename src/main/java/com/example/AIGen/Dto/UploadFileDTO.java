package com.example.AIGen.Dto;

import java.util.Date;
import java.util.List;

import com.example.AIGen.models.Project;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Id;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true) // Add this annotation
@Data
public class UploadFileDTO {
	

    private Date dateGenResume;

	private String description;

    private String fileId;
	private String title;

    private String projectId;
	private List<ParticipantDTO> participantsDTO;



	public String getFileId() {
		return fileId;
	}
	public void setFileId(String fileId) {
		this.fileId = fileId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Date getDateGenResume() {
		return dateGenResume;
	}
	public void setDateGenResume(Date dateGenResume) {
		this.dateGenResume = dateGenResume;
	}
	public List<ParticipantDTO> getParticipantsDTO() {
		return participantsDTO;
	}
	public void setParticipantsDTO(List<ParticipantDTO> participantsDTO) {
		this.participantsDTO = participantsDTO;
	}
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}


	
	
}
