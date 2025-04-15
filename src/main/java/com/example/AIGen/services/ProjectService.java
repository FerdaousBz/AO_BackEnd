package com.example.AIGen.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.AIGen.Dto.ProjectDTO;
import com.example.AIGen.Repository.ProjectRepository;
//import com.example.AIGen.mappers.UploadFileMapper;
import com.example.AIGen.models.Project;

@Service
public class ProjectService {

	@Autowired
	private ProjectRepository projectRepository;

//	@Autowired
//	private UploadFileMapper uploadFileMapper;

	public Project createNewProject(Project project) {
	    Project savedProject = projectRepository.save(project);
	    savedProject.setProjectId(savedProject.get_id());  
	    return projectRepository.save(savedProject);
	}

    public Project getProjectById(String _id) {
        return projectRepository.findById(_id).orElse(null);
    }

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }
	
}
