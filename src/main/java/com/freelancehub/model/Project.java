package com.freelancehub.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "projects")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(columnDefinition = "TEXT")
    private String requiredSkills;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal budget;

    private LocalDate deadline;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.OPEN;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "password"})
    private User client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hired_freelancer_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "password"})
    private User hiredFreelancer;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Proposal> proposals;

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

    public enum Status { OPEN, IN_PROGRESS, COMPLETED, CANCELLED }

    public Project() {}

    // Getters
    public Long getId()                     { return id; }
    public String getTitle()                { return title; }
    public String getDescription()          { return description; }
    public String getRequiredSkills()       { return requiredSkills; }
    public BigDecimal getBudget()           { return budget; }
    public LocalDate getDeadline()          { return deadline; }
    public Status getStatus()               { return status; }
    public User getClient()                 { return client; }
    public User getHiredFreelancer()        { return hiredFreelancer; }
    public List<Proposal> getProposals()    { return proposals; }
    public LocalDateTime getCreatedAt()     { return createdAt; }
    public LocalDateTime getUpdatedAt()     { return updatedAt; }

    // Setters
    public void setId(Long id)                           { this.id = id; }
    public void setTitle(String title)                   { this.title = title; }
    public void setDescription(String description)       { this.description = description; }
    public void setRequiredSkills(String requiredSkills) { this.requiredSkills = requiredSkills; }
    public void setBudget(BigDecimal budget)             { this.budget = budget; }
    public void setDeadline(LocalDate deadline)          { this.deadline = deadline; }
    public void setStatus(Status status)                 { this.status = status; }
    public void setClient(User client)                   { this.client = client; }
    public void setHiredFreelancer(User u)               { this.hiredFreelancer = u; }
    public void setProposals(List<Proposal> proposals)   { this.proposals = proposals; }

    // Builder
    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private final Project p = new Project();
        public Builder title(String v)          { p.title = v;         return this; }
        public Builder description(String v)    { p.description = v;   return this; }
        public Builder requiredSkills(String v) { p.requiredSkills = v; return this; }
        public Builder budget(BigDecimal v)     { p.budget = v;        return this; }
        public Builder deadline(LocalDate v)    { p.deadline = v;      return this; }
        public Builder status(Status v)         { p.status = v;        return this; }
        public Builder client(User v)           { p.client = v;        return this; }
        public Project build()                  { return p; }
    }
}
