package com.iam.mini_iam.repository;

import com.iam.mini_iam.entity.permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<permission, Long> {
}