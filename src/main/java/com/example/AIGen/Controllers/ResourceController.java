package com.example.AIGen.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.AIGen.Repository.RoleRepository;
import com.example.AIGen.models.ERole;
import com.example.AIGen.models.Role;
import com.example.AIGen.payload.request.RoleContent;

@RestController
@RequestMapping("/api/resource")
@CrossOrigin
public class ResourceController {

    @Autowired
    private RoleRepository roleRepository;

    @PostMapping("/addrole")
    public ResponseEntity<String> addRole(@RequestBody RoleContent roleRequest) {
        String roleName = roleRequest.getRole().toUpperCase(); // Convert to uppercase to match enum

        // Check if the role already exists
        if (roleRepository.existsByName(ERole.valueOf(roleName))) {
            return ResponseEntity.badRequest().body("Error: Role already exists!");
        }

        // Create new role
        Role role = new Role(ERole.valueOf(roleName));
        roleRepository.save(role);

        return ResponseEntity.ok("Role added successfully!");
    }

	@GetMapping("/admin")
	@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> adminAccess() {
        return ResponseEntity.ok("Admin access only");
    }
	
    @GetMapping("/vp")
    @PreAuthorize("hasRole('VP')")
    public ResponseEntity<String> vpAccess() {
        return ResponseEntity.ok("VP access only");
    }

    @GetMapping("/manager")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<String> managerAccess() {
    	 return ResponseEntity.ok("Manager access only");
    }
    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> userAccess() {
    	 return ResponseEntity.ok("User access only");
    }
}
