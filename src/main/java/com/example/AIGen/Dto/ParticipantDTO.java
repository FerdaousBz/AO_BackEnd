package com.example.AIGen.Dto;

import lombok.Data;

@Data
public class ParticipantDTO {

	private String _id;
	private String participantName;
	private String fonction;	
	private String email;
	
	private String fileId;
	 
	
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getFileId() {
		return fileId;
	}
	public void setFileId(String fileId) {
		this.fileId = fileId;
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
	
}
