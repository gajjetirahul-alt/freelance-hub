package com.freelancehub.controller;

import com.freelancehub.model.Message;
import com.freelancehub.service.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping
    public ResponseEntity<Message> send(
            @RequestBody Map<String, Object> body,
            @AuthenticationPrincipal UserDetails principal) {
        Long receiverId = Long.parseLong(body.get("receiverId").toString());
        String content  = body.get("content").toString();
        Long projectId  = body.containsKey("projectId") && body.get("projectId") != null
                          ? Long.parseLong(body.get("projectId").toString()) : null;
        return ResponseEntity.status(201).body(
                messageService.send(principal.getUsername(), receiverId, content, projectId));
    }

    @GetMapping("/conversation/{otherUserId}")
    public ResponseEntity<List<Message>> conversation(
            @PathVariable Long otherUserId,
            @AuthenticationPrincipal UserDetails principal) {
        return ResponseEntity.ok(messageService.getConversation(principal.getUsername(), otherUserId));
    }

    @GetMapping("/partners")
    public ResponseEntity<List<Long>> partners(@AuthenticationPrincipal UserDetails principal) {
        return ResponseEntity.ok(messageService.getConversationPartners(principal.getUsername()));
    }

    @PatchMapping("/{id}/read")
    public ResponseEntity<Void> markRead(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails principal) {
        messageService.markRead(id, principal.getUsername());
        return ResponseEntity.noContent().build();
    }
}
