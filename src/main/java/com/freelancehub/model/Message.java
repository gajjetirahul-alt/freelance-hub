package com.freelancehub.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(nullable = false, updatable = false)
    private LocalDateTime timestamp;

    @Column(nullable = false)
    private Boolean isRead = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "password"})
    private User sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "password"})
    private User receiver;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "proposals"})
    private Project project;

    @PrePersist
    protected void onCreate() { timestamp = LocalDateTime.now(); }

    public Message() {}

    // Getters
    public Long getId()                 { return id; }
    public String getContent()          { return content; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public Boolean getIsRead()          { return isRead; }
    public User getSender()             { return sender; }
    public User getReceiver()           { return receiver; }
    public Project getProject()         { return project; }

    // Setters
    public void setId(Long id)              { this.id = id; }
    public void setContent(String content)  { this.content = content; }
    public void setIsRead(Boolean isRead)   { this.isRead = isRead; }
    public void setSender(User sender)      { this.sender = sender; }
    public void setReceiver(User receiver)  { this.receiver = receiver; }
    public void setProject(Project project) { this.project = project; }

    // Builder
    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private final Message m = new Message();
        public Builder content(String v)    { m.content = v;  return this; }
        public Builder sender(User v)       { m.sender = v;   return this; }
        public Builder receiver(User v)     { m.receiver = v; return this; }
        public Builder project(Project v)   { m.project = v;  return this; }
        public Message build()              { return m; }
    }
}
