package com.example.AIGen.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.AIGen.Dto.ProjectDTO;
import com.example.AIGen.models.Project;
import com.example.AIGen.services.ProjectService;

@RestController
@RequestMapping("/api/project")
public class ProjectController {
	
	 @Autowired
	  private ProjectService projectService;

	    @PostMapping("/addproject")
	    public ResponseEntity<Project> createProject(@RequestBody Project project) {
	    	Project savedProject = projectService.createNewProject(project);
	        return ResponseEntity.ok(savedProject);
	    }

	    @GetMapping("/{projectId}")
	    public ResponseEntity<Project> getProjectById(@PathVariable String _id) {
	        Project project = projectService.getProjectById(_id);
	        return ResponseEntity.ok(project);
	    }

	    @GetMapping
	    public ResponseEntity<List<Project>> getAllProjects() {
	        List<Project> projects = projectService.getAllProjects();
	        return ResponseEntity.ok(projects);
	    }
	 
}
