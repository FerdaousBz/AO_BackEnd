package com.example.AIGen.models;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.persistence.CascadeType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;
@Data
@Document(collection = "participants")
public class Participants {

	@Id
	private String _id;
	
	private String participantName;
	private String fonction;
	private String email;
	
	@DBRef
	private UploadFile uploadFile;
	


	public UploadFile getUploadFile() {
		return uploadFile;
	}

	public void setUploadFile(UploadFile uploadFile) {
		this.uploadFile = uploadFile;
	}

	public String getParticipantId() {
		return _id;
	}

	public void setParticipantId(String _id) {
		this._id = _id;
	}

	public String getParticipantName() {
		return participantName;
	}

	public void setParticipantName(String participantName) {
		this.participantName = participantName;
	}

	public String getFonction() {
		return fonction;
	}

	public void setFonction(String fonction) {
		this.fonction = fonction;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

//	public List<Resumes> getResumeId() {
//		return resumeId;
//	}
//
//	public void setResumeId(List<Resumes> resumeId) {
//		this.resumeId = resumeId;
//	}


	
	
}
