package com.example.LocalZero.service.registration;

import com.example.LocalZero.dto.RegisterRequest;
import com.example.LocalZero.model.Role;
import com.example.LocalZero.model.User;
import com.example.LocalZero.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ResidentRegistrationService extends UserRegistrationTemplate {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public ResidentRegistrationService(UserRepository userRepository,
                                       PasswordEncoder passwordEncoder) {
        this.userRepository  = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void validateInput(RegisterRequest request) {
        if (userRepository.existByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already in use");
        }
    }

    @Override
    protected void hashAndSetPassword(User user, String rawPassword) {
        user.setPassword(passwordEncoder.encode(rawPassword));
    }

    @Override
    protected void assignRole(User user) {
        user.getRoles().add(Role.RESIDENT);
    }

    @Override
    protected synchronized User saveUser(User user) {
        return userRepository.save(user);
    }
}
