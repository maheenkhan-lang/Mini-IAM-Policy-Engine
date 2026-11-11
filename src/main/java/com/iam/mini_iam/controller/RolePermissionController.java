package com.iam.mini_iam.controller;

import com.iam.mini_iam.entity.Role;
import com.iam.mini_iam.entity.permission;
import com.iam.mini_iam.repository.RoleRepository;
import com.iam.mini_iam.repository.PermissionRepository;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/roles")
public class RolePermissionController {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    public RolePermissionController(
            RoleRepository roleRepository,
            PermissionRepository permissionRepository) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }

    // Assign permission to role
    @PostMapping("/{roleId}/permissions/{permissionId}")
    public ResponseEntity<Role> assignPermission(
            @PathVariable Long roleId,
            @PathVariable Long permissionId) {

        var roleOptional = roleRepository.findById(roleId);
        var permissionOptional = permissionRepository.findById(permissionId);

        if (roleOptional.isEmpty() || permissionOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Role role = roleOptional.get();
        permission newPermission = permissionOptional.get();

        role.getPermissions().add(newPermission);

        return ResponseEntity.ok(roleRepository.save(role));
    }
}