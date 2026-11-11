package com.iam.mini_iam.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String action;
    private String resource;
    private boolean allowed;
    private String reason;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public AuditLog() {
    }

    public AuditLog(String username, String action,
                    String resource, boolean allowed,
                    String reason) {
        this.username = username;
        this.action = action;
        this.resource = resource;
        this.allowed = allowed;
        this.reason = reason;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getAction() { return action; }
    public String getResource() { return resource; }
    public boolean isAllowed() { return allowed; }
    public String getReason() { return reason; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}