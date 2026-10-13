package com.freelancehub.controller;

import com.freelancehub.model.Project;
import com.freelancehub.service.ProjectService;
import com.freelancehub.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;
    private final UserService userService;

    public ProjectController(ProjectService projectService, UserService userService) {
        this.projectService = projectService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<Page<Project>> list(
            @RequestParam(required = false) Project.Status status,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(projectService.search(status, keyword, page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Project> get(@PathVariable Long id) {
        return ResponseEntity.ok(projectService.getById(id));
    }

    @GetMapping("/mine")
    public ResponseEntity<Page<Project>> mine(
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal UserDetails principal) {
        Long clientId = userService.findByEmail(principal.getUsername()).getId();
        return ResponseEntity.ok(projectService.getByClient(clientId, page, size));
    }

    @PostMapping
    public ResponseEntity<Project> create(
            @RequestBody Map<String, Object> body,
            @AuthenticationPrincipal UserDetails principal) {
        String title       = (String) body.get("title");
        String description = (String) body.get("description");
        String skills      = (String) body.getOrDefault("requiredSkills", "");
        BigDecimal budget  = new BigDecimal(body.get("budget").toString());
        LocalDate deadline = body.containsKey("deadline")
                ? LocalDate.parse(body.get("deadline").toString()) : null;
        Project project = projectService.createProject(
                title, description, skills, budget, deadline, principal.getUsername());
        return ResponseEntity.status(201).body(project);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Project> update(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates,
            @AuthenticationPrincipal UserDetails principal) {
        return ResponseEntity.ok(projectService.updateProject(id, updates, principal.getUsername()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails principal) {
        projectService.deleteProject(id, principal.getUsername());
        return ResponseEntity.noContent().build();
    }
}
