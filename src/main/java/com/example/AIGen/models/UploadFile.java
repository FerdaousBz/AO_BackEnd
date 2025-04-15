package com.example.AIGen.models;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonProperty;

@Document(collection = "uploadedFiles")
public class UploadFile {
	
    @Id
    @JsonProperty("_id") // Maps the MongoDB _id field to this property
    private String id;
    @JsonProperty("file_id") 
    private String fileId;
    private String title;
    private String description;
    @JsonProperty("date_gen_résumé") 
    private Date dateGenResume;
    
    public UploadFile() {
    	
    }
	public UploadFile(String fileId, String title, String description, Date dateGenResume) {
		super();
		this.fileId = fileId;
		this.title = title;
		this.description = description;
		this.dateGenResume = dateGenResume;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
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


}
