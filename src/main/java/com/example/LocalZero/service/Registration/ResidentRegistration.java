package com.example.LocalZero.service.Registration;

import com.example.LocalZero.Model.Role;
import com.example.LocalZero.Model.User;
import com.example.LocalZero.dto.RegisterRequest;
import com.example.LocalZero.exception.ValidationException;
import com.example.LocalZero.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ResidentRegistration extends UserRegistrationTemplate {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    protected void validateInput(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ValidationException("Email already in use: " + request.getEmail());
        }
    }

    @Override
    protected void hashAndSetPassword(User user, String password) {
        user.setPassword(passwordEncoder.encode(password));
    }

    @Override
    protected void assignRole(User user) {
        List<Role> roles = new ArrayList<>();
        roles.add(Role.RESIDENT);
        user.setRoles(roles);
    }

    @Override
    protected User saveUser(User user) {
        try {
            return userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            throw new ValidationException("Email already in use: " + user.getEmail());
        }
    }
}
