package com.iam.mini_iam.controller;

import com.iam.mini_iam.dto.LoginRequest;
import com.iam.mini_iam.entity.User;
import com.iam.mini_iam.repository.UserRepository;
import com.iam.mini_iam.service.PasswordService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.iam.mini_iam.service.JwtService;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class LoginController {

    private final UserRepository userRepository;
    private final PasswordService passwordService;
    private final JwtService jwtService;

    public LoginController(UserRepository userRepository,
                       PasswordService passwordService,
                       JwtService jwtService) {
    this.userRepository = userRepository;
    this.passwordService = passwordService;
    this.jwtService = jwtService;
}

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        User user = userRepository.findByUsername(request.username())
                .orElse(null);

        if (user == null || user.getPassword() == null ||
                !passwordService.verifyPassword(
                        request.password(), user.getPassword())) {

            return ResponseEntity.status(401)
                    .body(Map.of("message", "Invalid username or password"));
        }

       String token = jwtService.generateToken(user.getUsername());

return ResponseEntity.ok(Map.of(
        "message", "Login successful",
        "token", token,
        "username", user.getUsername(),
        "email", user.getEmail(),
        "department", user.getDepartment()
));
    }
}