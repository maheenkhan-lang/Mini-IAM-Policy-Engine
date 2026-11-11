package com.iam.mini_iam.service;

import com.iam.mini_iam.entity.User;
import com.iam.mini_iam.entity.Role;
import com.iam.mini_iam.entity.permission;
import com.iam.mini_iam.repository.UserRepository;

import org.springframework.stereotype.Service;

import java.time.LocalTime;

@Service
public class PolicyEngine {

    private final UserRepository userRepository;

    public PolicyEngine(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public AccessDecision checkAccess(
            String username,
            String action,
            String resource,
            String department) {

        User user = userRepository.findByUsername(username)
                .orElse(null);

        // DENY if user does not exist
        if (user == null) {
            return new AccessDecision(false,
                    "User not found",
                    username, action, resource);
        }

        boolean haspermission = false;

        // Check user's roles and their permissions
        for (Role role : user.getRoles()) {

            for (permission permission : role.getpermissions()) {

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

        // DENY if no matching permission exists
        if (!haspermission) {
            return new AccessDecision(false,
                    "No matching role permission",
                    username, action, resource);
        }

        // ABAC: Department restriction
        if (department != null && !department.isBlank()) {

            if (!department.equalsIgnoreCase(user.getDepartment())) {
                return new AccessDecision(false,
                        "Department mismatch",
                        username, action, resource);
            }
        }

        // ABAC: Working hours (9 AM to 5 PM)
        LocalTime now = LocalTime.now();

        if (now.isBefore(LocalTime.of(9, 0))
                || now.isAfter(LocalTime.of(17, 0))) {

            return new AccessDecision(false,
                    "Outside working hours",
                    username, action, resource);
        }

        // Access granted
        return new AccessDecision(true,
                "Access granted",
                username, action, resource);
    }
}