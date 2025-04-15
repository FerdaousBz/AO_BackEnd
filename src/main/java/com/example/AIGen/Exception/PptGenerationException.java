package com.example.AIGen.Exception;

public class PptGenerationException extends RuntimeException {

	 private final String ppt_id;

	    public PptGenerationException(String message, String ppt_id) {
	        super(message);
	        this.ppt_id = ppt_id;
	    }

	    public String getPptId() {
	        return ppt_id;
	    }
}
