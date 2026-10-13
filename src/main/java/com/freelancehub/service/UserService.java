package com.freelancehub.service;

import com.freelancehub.model.User;
import com.freelancehub.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserService {

    private final UserRepository userRepo;

    public UserService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    public User findById(Long id) {
        return userRepo.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + id));
    }

    public User findByEmail(String email) {
        return userRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));
    }

    public User updateProfile(Long userId, Map<String, Object> updates) {
        User user = findById(userId);
        if (updates.containsKey("name"))       user.setName((String) updates.get("name"));
        if (updates.containsKey("title"))      user.setTitle((String) updates.get("title"));
        if (updates.containsKey("bio"))        user.setBio((String) updates.get("bio"));
        if (updates.containsKey("skills"))     user.setSkills((String) updates.get("skills"));
        if (updates.containsKey("portfolio"))  user.setPortfolio((String) updates.get("portfolio"));
        if (updates.containsKey("experience")) user.setExperience((String) updates.get("experience"));
        if (updates.containsKey("hourlyRate")) {
            user.setHourlyRate(Double.parseDouble(updates.get("hourlyRate").toString()));
        }
        return userRepo.save(user);
    }
}
