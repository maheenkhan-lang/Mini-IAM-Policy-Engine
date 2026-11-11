package com.iam.mini_iam.config;

import com.iam.mini_iam.entity.User;
import com.iam.mini_iam.repository.UserRepository;
import com.iam.mini_iam.service.PasswordService;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class PasswordSetupRunner implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordService passwordService;

    public PasswordSetupRunner(
            UserRepository userRepository,
            PasswordService passwordService) {
        this.userRepository = userRepository;
        this.passwordService = passwordService;
    }

    @Override
    public void run(String... args) {

        setupPassword("admin", System.getenv("ADMIN_INITIAL_PASSWORD"));
        setupPassword("developer", System.getenv("DEVELOPER_INITIAL_PASSWORD"));
        setupPassword("viewer", System.getenv("VIEWER_INITIAL_PASSWORD"));
    }
private void setupPassword(String username, String password) {

    User user = userRepository.findByUsername(username)
            .orElseThrow(() ->
                    new IllegalStateException("User not found: " + username));

    if (user.getPassword() == null) {

        if (password == null || password.isBlank()) {
            throw new IllegalStateException(
                    "Missing initial password for " + username);
        }

        user.setPassword(passwordService.hashPassword(password));
        userRepository.save(user);

        System.out.println("Password configured for: " + username);
    }
}
    
}