package com.example.LocalZero.service.deleteaccount;


import com.example.LocalZero.Model.BlacklistedToken;
import com.example.LocalZero.Model.User;
import com.example.LocalZero.exception.InvalidCredentialException;
import com.example.LocalZero.repository.BlacklistedTokenRepository;
import com.example.LocalZero.repository.UserRepository;
import com.example.LocalZero.service.IAccountDeletedNotification;
import com.example.LocalZero.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service("userDeleteAccount")
@RequiredArgsConstructor
public class UserDeleteAccount extends UserDeleteAccountTemplate {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final BlacklistedTokenRepository blacklistedTokenRepository;
    private final IAccountDeletedNotification accountDeletedNotification;


    @Override
    protected String extractEmail(String token) {
        return jwtService.extractEmail(token);
    }

    @Override
    protected User findUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new InvalidCredentialException("Invalid credentials"));
    }

    @Override
    protected void verifyPassword(String rawPassword, String hashPassword) {
        if (!passwordEncoder.matches(rawPassword, hashPassword)) {
            throw new InvalidCredentialException("Invalid credential");
        }
    }

    @Override
    protected void blacklistToken(String token) {
        Date expiresAt = jwtService.extractExpiration(token);
        LocalDateTime expiresAtLDT = expiresAt.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        blacklistedTokenRepository.save(new BlacklistedToken(token, LocalDateTime.now(), expiresAtLDT));
    }

    @Override
    protected void deleteUser(User user) {
        userRepository.delete(user);
    }

    @Override
    protected void sendNotification(User user) {
        accountDeletedNotification.notify(user.getEmail(), user.getName());
    }
}
