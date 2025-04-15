package com.example.AIGen.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;

import com.example.AIGen.Repository.RoleRepository;
import com.example.AIGen.models.ERole;
import com.example.AIGen.models.Role;

public class DataInitializer implements ApplicationListener<ApplicationReadyEvent>{
    
	@Autowired
    private RoleRepository roleRepository;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        // Create default roles if they don't exist
        addRoleIfNotExists(ERole.ROLE_USER);
        addRoleIfNotExists(ERole.ROLE_ADMIN);
        addRoleIfNotExists(ERole.ROLE_VP);
    }

    private void addRoleIfNotExists(ERole roleName) {
        if (!roleRepository.existsByName(roleName)) {
            Role role = new Role(roleName);
            roleRepository.save(role);
        }
    }
}
