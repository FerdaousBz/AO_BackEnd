package com.example.AIGen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
@SpringBootApplication
public class AiGenApplication {

//    public static void main(String[] args) {
//        SpringApplication app = new SpringApplication(AiGenApplication.class);
//        Environment env = app.run(args).getEnvironment();
//
//        // Log the active profiles
//        System.out.println("Active Profiles: " + String.join(", ", env.getActiveProfiles()));
//    }
	public static void main(String[] args) {
		SpringApplication.run(AiGenApplication.class, args);
	}

}
