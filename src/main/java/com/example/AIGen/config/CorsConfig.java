package com.example.AIGen.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {
	
	private static final List<String> ALLOWED_ORIGINS = List.of(
			"http://localhost:3000" // local development frontend
			
			);
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins(ALLOWED_ORIGINS.toArray(new String[0])) // Specify the allowed origins
                        .allowedMethods("GET", "POST","PATCH", "PUT", "DELETE") // Specify the allowed methods
                        .allowedHeaders("*") // Allow all headers
                        .allowCredentials(true)
                        .maxAge(3600); // Max age for the browser to cache the CORS preflight response
            }
        };
    }

}
