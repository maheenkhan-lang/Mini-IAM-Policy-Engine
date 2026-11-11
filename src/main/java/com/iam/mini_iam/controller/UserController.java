package com.iam.mini_iam.controller;

import com.iam.mini_iam.entity.User;
import com.iam.mini_iam.repository.UserRepository;
import com.iam.mini_iam.repository.RoleRepository;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import com.iam.mini_iam.service.PasswordService;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordService passwordService;

   public UserController(UserRepository userRepository,
                      RoleRepository roleRepository,
                      PasswordService passwordService) {
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
    this.passwordService = passwordService;
}

    // Create a new user
    @PostMapping
public User createUser(@RequestBody User user) {

    String hashedPassword =
            passwordService.hashPassword(user.getPassword());

    user.setPassword(hashedPassword);

    return userRepository.save(user);
}

    // Get all users
    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Get user by ID
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Delete user
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if (!userRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        userRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // Assign a role to a user
    @PostMapping("/{userId}/roles/{roleId}")
    public ResponseEntity<User> assignRole(
            @PathVariable Long userId,
            @PathVariable Long roleId) {

        var userOptional = userRepository.findById(userId);

        if (userOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var roleOptional = roleRepository.findById(roleId);

        if (roleOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User user = userOptional.get();
        user.getRoles().add(roleOptional.get());

        return ResponseEntity.ok(userRepository.save(user));
    }
}