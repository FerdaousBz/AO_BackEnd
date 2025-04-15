package com.example.AIGen.payload.response;

public class PptGenratedResponse {
	
    private String statusCode;
    private String message;
    private String pptContent;
    private String pptId;
    private String pptUploadDate;
    private String pptName;

	private int version;
    

	public PptGenratedResponse(String statusCode, String message, String pptContent, String pptId, String pptUploadDate,
			String pptName,int version) {
		super();
		this.statusCode = statusCode;
		this.message = message;
		this.pptContent = pptContent;
		this.pptId = pptId;
		this.pptUploadDate = pptUploadDate;
		this.pptName = pptName;
	
		this.version = version;
	}
	public String getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getPptContent() {
		return pptContent;
	}
	public void setPptContent(String pptContent) {
		this.pptContent = pptContent;
	}
	public String getPptId() {
		return pptId;
	}
	public void setPptId(String pptId) {
		this.pptId = pptId;
	}
	public String getPptUploadDate() {
		return pptUploadDate;
	}
	public void setPptUploadDate(String pptUploadDate) {
		this.pptUploadDate = pptUploadDate;
	}
	public String getPptName() {
		return pptName;
	}
	public void setPptName(String pptName) {
		this.pptName = pptName;
	}
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	   
    

}