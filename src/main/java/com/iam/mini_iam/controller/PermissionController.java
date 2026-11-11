package com.iam.mini_iam.controller;

import com.iam.mini_iam.entity.permission;
import com.iam.mini_iam.repository.PermissionRepository;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("/api/permissions")
public class PermissionController {

    private final PermissionRepository permissionRepository;

    public PermissionController(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    // Create a permission
    @PostMapping
    public permission createPermission(@RequestBody permission newPermission) {
        return permissionRepository.save(newPermission);
    }

    // Get all permissions
    @GetMapping
    public List<permission> getAllPermissions() {
        return permissionRepository.findAll();
    }

    // Get permission by ID
    @GetMapping("/{id}")
    public ResponseEntity<permission> getPermissionById(@PathVariable Long id) {
        return permissionRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Delete permission
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePermission(@PathVariable Long id) {
        if (!permissionRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        permissionRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}