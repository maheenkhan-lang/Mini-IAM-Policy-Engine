package com.iam.mini_iam.repository;

import com.iam.mini_iam.entity.CloudResource;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CloudResourceRepository
        extends JpaRepository<CloudResource, Long> {
}