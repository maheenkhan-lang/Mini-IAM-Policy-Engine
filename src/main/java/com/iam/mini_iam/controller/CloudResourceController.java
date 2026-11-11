package com.iam.mini_iam.controller;

import com.iam.mini_iam.entity.CloudResource;
import com.iam.mini_iam.repository.CloudResourceRepository;
import com.iam.mini_iam.service.PolicyEngine;
import com.iam.mini_iam.service.AccessDecision;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

@RestController
@RequestMapping("/api/resources")
public class CloudResourceController {

    private final CloudResourceRepository repository;
    private final PolicyEngine policyEngine;

    public CloudResourceController(
            CloudResourceRepository repository,
            PolicyEngine policyEngine) {
        this.repository = repository;
        this.policyEngine = policyEngine;
    }

    // Get the currently logged-in username from JWT
    private String getCurrentUsername() {
        return SecurityContextHolder.getContext()
                .getAuthentication().getName();
    }

    // Add a cloud resource
   @PostMapping
public ResponseEntity<?> addResource(
        @RequestBody CloudResource resource) {

    AccessDecision decision = policyEngine.checkAccess(
            getCurrentUsername(),
            "CREATE",
            resource.getType(),
            null);

    if (!decision.isAllowed()) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(decision);
    }

    return ResponseEntity.ok(repository.save(resource));
}

    // Get all cloud resources
   @GetMapping
public List<CloudResource> getAllResources() {

    String username = getCurrentUsername();

    return repository.findAll()
            .stream()
            .filter(resource ->
                    policyEngine.checkAccess(
                            username, "VIEW",
                            resource.getType(), null
                    ).isAllowed())
            .toList();
}

    // Get resource by ID
    @GetMapping("/{id}")
public ResponseEntity<?> getResourceById(
        @PathVariable Long id) {

    CloudResource resource = repository.findById(id)
            .orElse(null);

    if (resource == null) {
        return ResponseEntity.notFound().build();
    }

    AccessDecision decision = policyEngine.checkAccess(
            getCurrentUsername(), "VIEW",
            resource.getType(), null);

    if (!decision.isAllowed()) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(decision);
    }

    return ResponseEntity.ok(resource);
}

    // Start VM
    @PutMapping("/{id}/start")
    public ResponseEntity<?> startResource(
            @PathVariable Long id) {

        CloudResource resource = repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Resource not found"));

        AccessDecision decision = policyEngine.checkAccess(
                getCurrentUsername(), "START",
                resource.getType(), null);

        if (!decision.isAllowed()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(decision);
        }

        resource.setStatus("RUNNING");
        return ResponseEntity.ok(repository.save(resource));
    }

    // Stop VM
    @PutMapping("/{id}/stop")
    public ResponseEntity<?> stopResource(
            @PathVariable Long id) {

        CloudResource resource = repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Resource not found"));

        AccessDecision decision = policyEngine.checkAccess(
                getCurrentUsername(), "STOP",
                resource.getType(), null);

        if (!decision.isAllowed()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(decision);
        }

        resource.setStatus("STOPPED");
        return ResponseEntity.ok(repository.save(resource));
    }

    // Delete VM or other general resource
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteResource(
            @PathVariable Long id) {

        CloudResource resource = repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Resource not found"));

        AccessDecision decision = policyEngine.checkAccess(
                getCurrentUsername(), "DELETE",
                resource.getType(), null);

        if (!decision.isAllowed()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(decision);
        }

        repository.delete(resource);

        return ResponseEntity.ok("Resource deleted successfully");
    }

    // Create storage
    @PostMapping("/storage")
    public ResponseEntity<?> createStorage(
            @RequestBody CloudResource resource) {

        AccessDecision decision = policyEngine.checkAccess(
                getCurrentUsername(), "CREATE", "STORAGE", null);

        if (!decision.isAllowed()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(decision);
        }

        resource.setType("STORAGE");
        resource.setStatus("AVAILABLE");

        return ResponseEntity.ok(repository.save(resource));
    }

    // View storage
    @GetMapping("/storage")
    public ResponseEntity<?> getStorage() {

        AccessDecision decision = policyEngine.checkAccess(
                getCurrentUsername(), "VIEW", "STORAGE", null);

        if (!decision.isAllowed()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(decision);
        }

        List<CloudResource> storage = repository.findAll()
                .stream()
                .filter(resource ->
                        "STORAGE".equalsIgnoreCase(resource.getType()))
                .toList();

        return ResponseEntity.ok(storage);
    }

    // Delete storage
    @DeleteMapping("/storage/{id}")
    public ResponseEntity<?> deleteStorage(
            @PathVariable Long id) {

        CloudResource resource = repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Resource not found"));

        if (!"STORAGE".equalsIgnoreCase(resource.getType())) {
            return ResponseEntity.badRequest()
                    .body("This resource is not storage");
        }

        AccessDecision decision = policyEngine.checkAccess(
                getCurrentUsername(), "DELETE", "STORAGE", null);

        if (!decision.isAllowed()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(decision);
        }

        repository.delete(resource);

        return ResponseEntity.ok("Storage deleted successfully");
    }

    // Create database
    @PostMapping("/databases")
    public ResponseEntity<?> createDatabase(
            @RequestBody CloudResource resource) {

        AccessDecision decision = policyEngine.checkAccess(
                getCurrentUsername(), "CREATE", "DATABASE", null);

        if (!decision.isAllowed()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(decision);
        }

        resource.setType("DATABASE");
        resource.setStatus("AVAILABLE");

        return ResponseEntity.ok(repository.save(resource));
    }

    // View databases
    @GetMapping("/databases")
    public ResponseEntity<?> getDatabases() {

        AccessDecision decision = policyEngine.checkAccess(
                getCurrentUsername(), "VIEW", "DATABASE", null);

        if (!decision.isAllowed()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(decision);
        }

        List<CloudResource> databases = repository.findAll()
                .stream()
                .filter(resource ->
                        "DATABASE".equalsIgnoreCase(resource.getType()))
                .toList();

        return ResponseEntity.ok(databases);
    }

    // Delete database
    @DeleteMapping("/databases/{id}")
    public ResponseEntity<?> deleteDatabase(
            @PathVariable Long id) {

        CloudResource resource = repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Database not found"));

        if (!"DATABASE".equalsIgnoreCase(resource.getType())) {
            return ResponseEntity.badRequest()
                    .body("This resource is not a database");
        }

        AccessDecision decision = policyEngine.checkAccess(
                getCurrentUsername(), "DELETE", "DATABASE", null);

        if (!decision.isAllowed()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(decision);
        }

        repository.delete(resource);

        return ResponseEntity.ok("Database deleted successfully");
    }
}