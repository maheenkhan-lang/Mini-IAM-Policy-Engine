package com.iam.mini_iam.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "permissions")
public class permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String action;

    @Column(nullable = false)
    private String resource;

    private String description;

    public permission() {
    }

    public permission(String action, String resource, String description) {
        this.action = action;
        this.resource = resource;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}