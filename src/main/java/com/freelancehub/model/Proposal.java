package com.freelancehub.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "proposals",
       uniqueConstraints = @UniqueConstraint(columnNames = {"project_id", "freelancer_id"}))
public class Proposal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal bidAmount;

    @Column(nullable = false)
    private String estimatedTime;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.PENDING;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "proposals"})
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "freelancer_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "password"})
    private User freelancer;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() { createdAt = LocalDateTime.now(); }

    public enum Status { PENDING, ACCEPTED, REJECTED }

    public Proposal() {}

    // Getters
    public Long getId()                  { return id; }
    public BigDecimal getBidAmount()     { return bidAmount; }
    public String getEstimatedTime()     { return estimatedTime; }
    public String getMessage()           { return message; }
    public Status getStatus()            { return status; }
    public Project getProject()          { return project; }
    public User getFreelancer()          { return freelancer; }
    public LocalDateTime getCreatedAt()  { return createdAt; }

    // Setters
    public void setId(Long id)                       { this.id = id; }
    public void setBidAmount(BigDecimal bidAmount)   { this.bidAmount = bidAmount; }
    public void setEstimatedTime(String t)           { this.estimatedTime = t; }
    public void setMessage(String message)           { this.message = message; }
    public void setStatus(Status status)             { this.status = status; }
    public void setProject(Project project)          { this.project = project; }
    public void setFreelancer(User freelancer)       { this.freelancer = freelancer; }

    // Builder
    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private final Proposal p = new Proposal();
        public Builder bidAmount(BigDecimal v)   { p.bidAmount = v;     return this; }
        public Builder estimatedTime(String v)   { p.estimatedTime = v; return this; }
        public Builder message(String v)         { p.message = v;       return this; }
        public Builder status(Status v)          { p.status = v;        return this; }
        public Builder project(Project v)        { p.project = v;       return this; }
        public Builder freelancer(User v)        { p.freelancer = v;    return this; }
        public Proposal build()                  { return p; }
    }
}
