package com.freelancehub.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String bio;

    @Column(columnDefinition = "TEXT")
    private String skills;

    @Column(columnDefinition = "TEXT")
    private String portfolio;

    private String experience;
    private String profilePicture;
    private Double hourlyRate;

    @Column(updatable = false)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt  = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum Role { CLIENT, FREELANCER }

    // Constructors
    public User() {}

    // Getters
    public Long getId()                 { return id; }
    public String getName()             { return name; }
    public String getEmail()            { return email; }
    public String getPassword()         { return password; }
    public Role getRole()               { return role; }
    public String getTitle()            { return title; }
    public String getBio()              { return bio; }
    public String getSkills()           { return skills; }
    public String getPortfolio()        { return portfolio; }
    public String getExperience()       { return experience; }
    public String getProfilePicture()   { return profilePicture; }
    public Double getHourlyRate()       { return hourlyRate; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    // Setters
    public void setId(Long id)                         { this.id = id; }
    public void setName(String name)                   { this.name = name; }
    public void setEmail(String email)                 { this.email = email; }
    public void setPassword(String password)           { this.password = password; }
    public void setRole(Role role)                     { this.role = role; }
    public void setTitle(String title)                 { this.title = title; }
    public void setBio(String bio)                     { this.bio = bio; }
    public void setSkills(String skills)               { this.skills = skills; }
    public void setPortfolio(String portfolio)         { this.portfolio = portfolio; }
    public void setExperience(String experience)       { this.experience = experience; }
    public void setProfilePicture(String p)            { this.profilePicture = p; }
    public void setHourlyRate(Double hourlyRate)       { this.hourlyRate = hourlyRate; }

    // Builder
    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private final User u = new User();
        public Builder name(String v)       { u.name = v;       return this; }
        public Builder email(String v)      { u.email = v;      return this; }
        public Builder password(String v)   { u.password = v;   return this; }
        public Builder role(Role v)         { u.role = v;       return this; }
        public Builder title(String v)      { u.title = v;      return this; }
        public Builder bio(String v)        { u.bio = v;        return this; }
        public Builder skills(String v)     { u.skills = v;     return this; }
        public Builder hourlyRate(Double v) { u.hourlyRate = v; return this; }
        public User build()                 { return u; }
    }
}
