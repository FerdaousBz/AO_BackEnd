package com.example.AIGen.payload.request;

import java.util.List;

public class PromptRequest {
	
    private List<RoleContent> prompt;

    public List<RoleContent> getPrompt() {
        return prompt;
    }

    public void setPrompt(List<RoleContent> prompt) {
        this.prompt = prompt;
    }
}
