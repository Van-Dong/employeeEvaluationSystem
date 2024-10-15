package com.dongnv.employee_evaluation_system.configuration;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.dongnv.employee_evaluation_system.constant.UserRole;
import com.dongnv.employee_evaluation_system.model.User;
import com.dongnv.employee_evaluation_system.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class ApplicationInitConfig {
    private final String adminUsername = "admin";
    private final String adminPassword = "admin";

    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            log.info("APPLICATION IS STARTED");

            // Create default admin account
            if (!userRepository.existsByUsername(adminUsername)) {
                User user = User.builder()
                        .username(adminUsername)
                        .password(passwordEncoder.encode(adminPassword))
                        .role(UserRole.ADMIN)
                        .build();
                userRepository.save(user);
                log.info("Create admin account success, please change default password");
            }
        };
    }
}
