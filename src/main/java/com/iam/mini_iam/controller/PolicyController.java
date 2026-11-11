package com.iam.mini_iam.controller;

import com.iam.mini_iam.service.PolicyEngine;
import com.iam.mini_iam.service.AccessDecision;

import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@RestController
@RequestMapping("/api/policy")
public class PolicyController {

    private final PolicyEngine policyEngine;

    public PolicyController(PolicyEngine policyEngine) {
        this.policyEngine = policyEngine;
    }

    @PostMapping("/check")
    public AccessDecision checkAccess(
            @RequestParam String action,
            @RequestParam String resource) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();

        return policyEngine.checkAccess(
                username, action, resource, null);
    }
}