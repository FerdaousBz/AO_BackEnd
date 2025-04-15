package com.example.AIGen.services;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.AIGen.payload.request.PromptRequest;

@Service
public class AIService {
	
	private final RestTemplate restTemplate;

	@Value("${ai.api.url}")
    private String apiUrl;
	
	@Value("${ai.api.key}")
    private String apiKey;
	
	  public AIService(RestTemplate restTemplate) {
	        this.restTemplate = restTemplate;
	    }

    public String getAIResponse(PromptRequest prompt) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("api_key" ,"U2FsdGVkX19VadLCOWc1V5V1Hy9x2qyyDT0OSN+VB0A=");   

        HttpEntity<PromptRequest> entity = new HttpEntity<>(prompt, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                apiUrl,
                HttpMethod.POST,
                entity,
                String.class
        );

        return response.getBody();
    }

}
