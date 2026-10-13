package com.freelancehub.controller;

import com.freelancehub.model.Proposal;
import com.freelancehub.service.ProposalService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ProposalController {

    private final ProposalService proposalService;

    public ProposalController(ProposalService proposalService) {
        this.proposalService = proposalService;
    }

    @PostMapping("/projects/{projectId}/proposals")
    public ResponseEntity<Proposal> submit(
            @PathVariable Long projectId,
            @RequestBody Map<String, Object> body,
            @AuthenticationPrincipal UserDetails principal) {
        BigDecimal bid = new BigDecimal(body.get("bidAmount").toString());
        String time    = body.get("estimatedTime").toString();
        String message = body.get("message").toString();
        return ResponseEntity.status(201).body(
                proposalService.submit(projectId, bid, time, message, principal.getUsername()));
    }

    @GetMapping("/projects/{projectId}/proposals")
    public ResponseEntity<List<Proposal>> getByProject(
            @PathVariable Long projectId,
            @AuthenticationPrincipal UserDetails principal) {
        return ResponseEntity.ok(proposalService.getByProject(projectId, principal.getUsername()));
    }

    @GetMapping("/proposals/mine")
    public ResponseEntity<List<Proposal>> mine(@AuthenticationPrincipal UserDetails principal) {
        return ResponseEntity.ok(proposalService.getMyProposals(principal.getUsername()));
    }

    @PatchMapping("/proposals/{proposalId}/accept")
    public ResponseEntity<Proposal> accept(
            @PathVariable Long proposalId,
            @AuthenticationPrincipal UserDetails principal) {
        return ResponseEntity.ok(proposalService.accept(proposalId, principal.getUsername()));
    }
}
