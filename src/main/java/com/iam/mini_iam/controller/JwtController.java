package com.iam.mini_iam.controller;

import com.iam.mini_iam.service.JwtService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class JwtController {

    private final JwtService jwtService;

    public JwtController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @GetMapping("/validate")
    public Map<String, Object> validateToken(
            @RequestHeader("Authorization") String authorization) {

        String token = authorization.replace("Bearer ", "");

        boolean valid = jwtService.isTokenValid(token);

        return Map.of(
                "valid", valid,
                "message", valid ? "Token is valid" : "Invalid token"
        );
    }
}