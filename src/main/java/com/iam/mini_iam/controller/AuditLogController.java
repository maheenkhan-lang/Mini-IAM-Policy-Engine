package com.iam.mini_iam.controller;

import com.iam.mini_iam.entity.AuditLog;
import com.iam.mini_iam.repository.AuditLogRepository;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/audit-logs")
public class AuditLogController {

    private final AuditLogRepository auditLogRepository;

    public AuditLogController(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    // Get all audit logs
    @GetMapping
    public List<AuditLog> getAllAuditLogs() {
        return auditLogRepository.findAll();
    }

    // Get audit logs for a specific user
    @GetMapping("/user/{username}")
    public List<AuditLog> getLogsByUsername(
            @PathVariable String username) {
        return auditLogRepository.findAll()
                .stream()
                .filter(log -> log.getUsername().equalsIgnoreCase(username))
                .toList();
    }
}