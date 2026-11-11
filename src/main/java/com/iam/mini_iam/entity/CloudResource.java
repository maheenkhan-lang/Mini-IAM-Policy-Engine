package com.iam.mini_iam.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "cloud_resources")
public class CloudResource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String type;
    private String status;
    private String region;

    private LocalDateTime createdAt;

    public CloudResource() {
    }

    public CloudResource(String name, String type,
                         String status, String region) {
        this.name = name;
        this.type = type;
        this.status = status;
        this.region = region;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getType() { return type; }
    public String getStatus() { return status; }
    public String getRegion() { return region; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setName(String name) { this.name = name; }
    public void setType(String type) { this.type = type; }
    public void setStatus(String status) { this.status = status; }
    public void setRegion(String region) { this.region = region; }
}