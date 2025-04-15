package com.example.AIGen.models;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "resumes")
public class Resumes {

	@Id
	private String _id;
	
	private String title;
	private String description;
	private String specifications;
	private Date dateGenResume;
	private String version;
	
//	@DBRef
//	private List<Participants> participants;
//	
//	@DBRef
//	private Project project;
	
	
//	public List<Participants> getParticipants() {
//		return participants;
//	}
//	public void setParticipants(List<Participants> participants) {
//		this.participants = participants;
//	}
//	public Project getProject() {
//		return project;
//	}
//	public void setProject(Project project) {
//		this.project = project;
//	}
	public String getResumeId() {
		return _id;
	}
	public void setResumeId(String _id) {
		this._id = _id;
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
	
	
	
	
}
