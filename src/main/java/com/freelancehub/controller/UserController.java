package com.freelancehub.controller;

import com.freelancehub.model.User;
import com.freelancehub.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}/profile")
    public ResponseEntity<Map<String, Object>> getProfile(@PathVariable Long id) {
        User user = userService.findById(id);
        return ResponseEntity.ok(Map.of(
                "id",         user.getId(),
                "name",       user.getName(),
                "role",       user.getRole(),
                "title",      user.getTitle()      != null ? user.getTitle()      : "",
                "bio",        user.getBio()        != null ? user.getBio()        : "",
                "skills",     user.getSkills()     != null ? user.getSkills()     : "",
                "experience", user.getExperience() != null ? user.getExperience() : "",
                "hourlyRate", user.getHourlyRate() != null ? user.getHourlyRate() : 0
        ));
    }

    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> getMe(
            @AuthenticationPrincipal UserDetails principal) {
        User user = userService.findByEmail(principal.getUsername());
        return ResponseEntity.ok(Map.of(
                "id",    user.getId(),
                "name",  user.getName(),
                "email", user.getEmail(),
                "role",  user.getRole()
        ));
    }

    @PatchMapping("/me")
    public ResponseEntity<Map<String, Object>> updateMe(
            @RequestBody Map<String, Object> updates,
            @AuthenticationPrincipal UserDetails principal) {
        User user = userService.findByEmail(principal.getUsername());
        userService.updateProfile(user.getId(), updates);
        return ResponseEntity.ok(Map.of("message", "Profile updated."));
    }
}
