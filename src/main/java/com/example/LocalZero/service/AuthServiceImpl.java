package com.example.LocalZero.service;

import com.example.LocalZero.dto.AuthResponse;
import com.example.LocalZero.dto.ChangePasswordRequest;
import com.example.LocalZero.dto.DeleteAccountRequest;
import com.example.LocalZero.dto.LoginRequest;
import com.example.LocalZero.dto.RegisterRequest;
import com.example.LocalZero.model.BlacklistedToken;
import com.example.LocalZero.model.User;
import com.example.LocalZero.repository.BlacklistedTokenRepository;
import com.example.LocalZero.repository.UserRepository;
import com.example.LocalZero.security.JwtUtil;
import com.example.LocalZero.service.registration.ResidentRegistrationService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements IAuthService {

    private final ResidentRegistrationService residentRegistration;
    private final UserRepository userRepository;
    private final BlacklistedTokenRepository blacklistedTokenRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(ResidentRegistrationService residentRegistration,
                           UserRepository userRepository,
                           BlacklistedTokenRepository blacklistedTokenRepository,
                           JwtUtil jwtUtil,
                           PasswordEncoder passwordEncoder) {
        this.residentRegistration = residentRegistration;
        this.userRepository = userRepository;
        this.blacklistedTokenRepository = blacklistedTokenRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public AuthResponse register(RegisterRequest request) {
        User user = residentRegistration.register(request);
        String token = jwtUtil.generateToken(user.getEmail(), user.getRoles());
        return new AuthResponse(token, user.getEmail(), user.getRoles());
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getRoles());
        return new AuthResponse(token, user.getEmail(), user.getRoles());
    }

    @Override
        public void logout(String token) {
            blacklistedTokenRepository.save(new BlacklistedToken(token));
        }


    @Override
    public synchronized void changePassword(String email, ChangePasswordRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Incorrect current password");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    public synchronized void deleteAccount(String email, DeleteAccountRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Incorrect password");
        }

        userRepository.delete(user);
    }
}
