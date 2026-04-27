package com.example.LocalZero.service.login;

import com.example.LocalZero.Model.User;
import com.example.LocalZero.exception.ValidationException;
import com.example.LocalZero.repository.UserRepository;
import com.example.LocalZero.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service("userLogin")
@RequiredArgsConstructor
public class UserLogin extends UserLoginTemplate {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    protected User findUser(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new ValidationException("Invalid email or password"));
    }

    @Override
    protected void verifyPassword(String rawPassword, String hashedPassword) {
        if (!passwordEncoder.matches(rawPassword, hashedPassword)) {
            throw new ValidationException("Invalid email or password");
        }
    }

    @Override
    protected String generateToken(User user) {
        return jwtService.generateToken(user.getEmail(), user.getRoles());
    }
}
