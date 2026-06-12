package com.example.LocalZero.config;

import com.example.LocalZero.Model.Role;
import com.example.LocalZero.Model.User;
import com.example.LocalZero.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AdminAccountSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${localzero.admin.email}")
    private String adminEmail;

    @Value("${localzero.admin.password}")
    private String adminPassword;

    @Value("${localzero.admin.name}")
    private String adminName;

    @Value("${localzero.admin.location}")
    private String adminLocation;

    @Override
    public void run(String... args) {
        if (adminPassword == null || adminPassword.isBlank()) {
            return;
        }

        if (userRepository.existsByEmail(adminEmail)) {
            return;
        }

        User admin = new User();
        admin.setName(adminName);
        admin.setEmail(adminEmail);
        admin.setLocation(adminLocation);
        admin.setPassword(passwordEncoder.encode(adminPassword));

        List<Role> roles = new ArrayList<>();
        roles.add(Role.RESIDENT);
        roles.add(Role.ADMIN);
        admin.setRoles(roles);

        userRepository.save(admin);
    }
}
