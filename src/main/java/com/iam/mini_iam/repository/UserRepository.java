package com.iam.mini_iam.repository;

import com.iam.mini_iam.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {
@EntityGraph(attributePaths = "roles")
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);
}