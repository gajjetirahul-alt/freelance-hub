package com.freelancehub.service;

import com.freelancehub.model.Project;
import com.freelancehub.model.User;
import com.freelancehub.repository.ProjectRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@Service
public class ProjectService {

    private final ProjectRepository projectRepo;
    private final UserService userService;

    public ProjectService(ProjectRepository projectRepo, UserService userService) {
        this.projectRepo = projectRepo;
        this.userService = userService;
    }

    public Project createProject(String title, String description, String skills,
                                  BigDecimal budget, LocalDate deadline, String clientEmail) {
        User client = userService.findByEmail(clientEmail);
        if (client.getRole() != User.Role.CLIENT) {
            throw new AccessDeniedException("Only clients can post projects.");
        }
        Project project = Project.builder()
                .title(title).description(description).requiredSkills(skills)
                .budget(budget).deadline(deadline).status(Project.Status.OPEN)
                .client(client).build();
        return projectRepo.save(project);
    }

    public Page<Project> search(Project.Status status, String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return projectRepo.search(status, keyword, pageable);
    }

    public Project getById(Long id) {
        return projectRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Project not found: " + id));
    }

    public Page<Project> getByClient(Long clientId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return projectRepo.findByClientId(clientId, pageable);
    }

    public Project updateProject(Long id, Map<String, Object> updates, String email) {
        Project project = getById(id);
        if (!project.getClient().getEmail().equals(email)) {
            throw new AccessDeniedException("Not your project.");
        }
        if (updates.containsKey("title"))         project.setTitle((String) updates.get("title"));
        if (updates.containsKey("description"))   project.setDescription((String) updates.get("description"));
        if (updates.containsKey("requiredSkills"))project.setRequiredSkills((String) updates.get("requiredSkills"));
        if (updates.containsKey("status")) {
            project.setStatus(Project.Status.valueOf(updates.get("status").toString()));
        }
        return projectRepo.save(project);
    }

    public void deleteProject(Long id, String email) {
        Project project = getById(id);
        if (!project.getClient().getEmail().equals(email)) {
            throw new AccessDeniedException("Not your project.");
        }
        projectRepo.delete(project);
    }
}
