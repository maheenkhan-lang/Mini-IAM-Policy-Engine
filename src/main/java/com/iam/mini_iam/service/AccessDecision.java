package com.iam.mini_iam.service;

public class AccessDecision {

    private boolean allowed;
    private String reason;
    private String username;
    private String action;
    private String resource;

    public AccessDecision(boolean allowed, String reason,
                          String username, String action,
                          String resource) {
        this.allowed = allowed;
        this.reason = reason;
        this.username = username;
        this.action = action;
        this.resource = resource;
    }

    public boolean isAllowed() {
        return allowed;
    }

    public String getReason() {
        return reason;
    }

    public String getUsername() {
        return username;
    }

    public String getAction() {
        return action;
    }

    public String getResource() {
        return resource;
    }
}