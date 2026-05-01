package com.example.LocalZero.service.registration;

import com.example.LocalZero.Model.Role;
import com.example.LocalZero.Model.User;
import com.example.LocalZero.dto.RegisterRequest;
import com.example.LocalZero.exception.DuplicateResourceException;
import com.example.LocalZero.repository.UserRepository;
import com.example.LocalZero.service.IAccountCreatedNotification;
import com.example.LocalZero.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("userRegistration")
@RequiredArgsConstructor
public class UserRegistration extends UserRegistrationTemplate {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final IAccountCreatedNotification accountCreatedNotification;

    @Override
    protected void validateInput(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already in use: " + request.getEmail());
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
            throw new DuplicateResourceException("Email already in use: " + user.getEmail());
        }
    }

    @Override
    protected String generateToken(User user) {
        return jwtService.generateToken(user.getEmail(), user.getRoles());
    }

    @Override
    protected void sendNotification(User user) {
        accountCreatedNotification.notify(user.getEmail(), user.getName());
    }
}