package com.iam.mini_iam.service;

import com.iam.mini_iam.entity.User;
import com.iam.mini_iam.entity.Role;
import com.iam.mini_iam.entity.permission;
import com.iam.mini_iam.entity.AuditLog;

import com.iam.mini_iam.repository.UserRepository;
import com.iam.mini_iam.repository.AuditLogRepository;

import org.springframework.stereotype.Service;

import java.time.LocalTime;

@Service
public class PolicyEngine {

    private final UserRepository userRepository;
    private final AuditLogRepository auditLogRepository;

    public PolicyEngine(
            UserRepository userRepository,
            AuditLogRepository auditLogRepository) {

        this.userRepository = userRepository;
        this.auditLogRepository = auditLogRepository;
    }

    public AccessDecision checkAccess(
            String username,
            String action,
            String resource,
            String department) {

        User user = userRepository.findByUsername(username)
                .orElse(null);

        // Deny if user does not exist
        if (user == null) {
            return recordDecision(new AccessDecision(
                    false, "User not found",
                    username, action, resource));
        }

        boolean haspermission = false;

        // RBAC: Check user's roles and permissions
        for (Role role : user.getRoles()) {

            for (permission permission : role.getPermissions()) {

                if (permission.getAction().equalsIgnoreCase(action)
                        && permission.getResource().equalsIgnoreCase(resource)) {

                    haspermission = true;
                    break;
                }
            }

            if (haspermission) {
                break;
            }
        }

        // Deny if no matching permission exists
        if (!haspermission) {
            return recordDecision(new AccessDecision(
                    false, "No matching role permission",
                    username, action, resource));
        }

        // ABAC: Working hours restriction (9 AM to 5 PM)
        LocalTime currentTime = LocalTime.now();

        LocalTime startTime = LocalTime.of(9, 0);
        LocalTime endTime = LocalTime.of(17, 0);

        if (currentTime.isBefore(startTime)
                || !currentTime.isBefore(endTime)) {

            return recordDecision(new AccessDecision(
                    false,
                    "Access denied: Outside working hours (9 AM - 5 PM)",
                    username, action, resource));
        }

        // ABAC: Department restriction
        if (department != null && !department.isBlank()) {

            if (!department.equalsIgnoreCase(user.getDepartment())) {
                return recordDecision(new AccessDecision(
                        false, "Department mismatch",
                        username, action, resource));
            }
        }

        // Access granted
        return recordDecision(new AccessDecision(
                true, "Access granted",
                username, action, resource));
    }

    // Save every access decision in audit logs
    private AccessDecision recordDecision(AccessDecision decision) {

        AuditLog log = new AuditLog(
                decision.getUsername(),
                decision.getAction(),
                decision.getResource(),
                decision.isAllowed(),
                decision.getReason()
        );

        auditLogRepository.save(log);

        return decision;
    }
}