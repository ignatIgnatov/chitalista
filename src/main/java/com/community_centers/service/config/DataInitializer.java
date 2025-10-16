package com.community_centers.service.config;

import com.community_centers.service.entity.User;
import com.community_centers.service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        if (!userRepository.existsByUsername("admin")) {
            User admin = new User();
            admin.setUsername("admin");

            String rawPassword = "admin123";
            String encodedPassword = passwordEncoder.encode(rawPassword);
            admin.setPassword(encodedPassword);

            admin.setRole("ROLE_ADMIN");
            admin.setEnabled(true);

            userRepository.save(admin);
        }
    }
}