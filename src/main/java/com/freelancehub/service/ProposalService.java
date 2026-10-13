package com.freelancehub.service;

import com.freelancehub.model.*;
import com.freelancehub.repository.ProjectRepository;
import com.freelancehub.repository.ProposalRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProposalService {

    private final ProposalRepository proposalRepo;
    private final ProjectRepository  projectRepo;
    private final UserService        userService;
    private final ProjectService     projectService;

    public ProposalService(ProposalRepository proposalRepo, ProjectRepository projectRepo,
                           UserService userService, ProjectService projectService) {
        this.proposalRepo = proposalRepo;
        this.projectRepo  = projectRepo;
        this.userService  = userService;
        this.projectService = projectService;
    }

    @Transactional
    public Proposal submit(Long projectId, BigDecimal bid, String estimatedTime,
                           String message, String freelancerEmail) {
        User freelancer = userService.findByEmail(freelancerEmail);
        if (freelancer.getRole() != User.Role.FREELANCER) {
            throw new AccessDeniedException("Only freelancers can submit proposals.");
        }
        Project project = projectService.getById(projectId);
        if (project.getStatus() != Project.Status.OPEN) {
            throw new IllegalStateException("Project is not accepting proposals.");
        }
        if (proposalRepo.existsByProjectIdAndFreelancerId(projectId, freelancer.getId())) {
            throw new IllegalStateException("You already submitted a proposal for this project.");
        }
        Proposal proposal = Proposal.builder()
                .project(project).freelancer(freelancer)
                .bidAmount(bid).estimatedTime(estimatedTime)
                .message(message).status(Proposal.Status.PENDING).build();
        return proposalRepo.save(proposal);
    }

    public List<Proposal> getByProject(Long projectId, String email) {
        User user = userService.findByEmail(email);
        Project project = projectService.getById(projectId);
        if (user.getRole() == User.Role.CLIENT) {
            if (!project.getClient().getId().equals(user.getId())) {
                throw new AccessDeniedException("Not your project.");
            }
            return proposalRepo.findByProjectId(projectId);
        }
        return proposalRepo.findByFreelancerId(user.getId()).stream()
                .filter(p -> p.getProject().getId().equals(projectId)).toList();
    }

    public List<Proposal> getMyProposals(String email) {
        User freelancer = userService.findByEmail(email);
        return proposalRepo.findByFreelancerId(freelancer.getId());
    }

    @Transactional
    public Proposal accept(Long proposalId, String clientEmail) {
        Proposal proposal = proposalRepo.findById(proposalId)
                .orElseThrow(() -> new EntityNotFoundException("Proposal not found"));
        Project project = proposal.getProject();
        User client = userService.findByEmail(clientEmail);
        if (!project.getClient().getId().equals(client.getId())) {
            throw new AccessDeniedException("Not your project.");
        }
        proposal.setStatus(Proposal.Status.ACCEPTED);
        proposalRepo.save(proposal);

        proposalRepo.findByProjectId(project.getId()).stream()
                .filter(p -> !p.getId().equals(proposalId))
                .forEach(p -> { p.setStatus(Proposal.Status.REJECTED); proposalRepo.save(p); });

        project.setStatus(Project.Status.IN_PROGRESS);
        project.setHiredFreelancer(proposal.getFreelancer());
        projectRepo.save(project);
        return proposal;
    }
}
