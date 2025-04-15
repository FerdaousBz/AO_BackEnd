package com.example.AIGen.Dto;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class ResumesDTO {

	  	private String resumeId;
	    private String title;
	    private String description;
	    private String specifications;
	    private Date dateGenResume;
	    private String version;
	    
//	    private List<ParticipantDTO> participants;
//	    private ProjectDTO project;
	    
		public String getResumeId() {
			return resumeId;
		}
		public void setResumeId(String resumeId) {
			this.resumeId = resumeId;
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
		public String getSpecifications() {
			return specifications;
		}
		public void setSpecifications(String specifications) {
			this.specifications = specifications;
		}
		public Date getDateGenResume() {
			return dateGenResume;
		}
		public void setDateGenResume(Date dateGenResume) {
			this.dateGenResume = dateGenResume;
		}
		public String getVersion() {
			return version;
		}
		public void setVersion(String version) {
			this.version = version;
		}
//		public List<ParticipantDTO> getParticipants() {
//			return participants;
//		}
//		public void setParticipants(List<ParticipantDTO> participants) {
//			this.participants = participants;
//		}
//		public ProjectDTO getProject() {
//			return project;
//		}
//		public void setProject(ProjectDTO project) {
//			this.project = project;
//		}
	
	
	
	
	
	
}
