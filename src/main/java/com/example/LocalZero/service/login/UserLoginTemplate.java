package com.example.LocalZero.service.login;

import com.example.LocalZero.Model.User;
import com.example.LocalZero.dto.AuthResponse;
import com.example.LocalZero.dto.LoginRequest;

public abstract class UserLoginTemplate {

    public final AuthResponse login(LoginRequest request) {
        User user = findUser(request.getEmail());
        verifyPassword(request.getPassword(), user.getPassword());
        String token = generateToken(user);
        return new AuthResponse(user.getId(), user.getName(), user.getEmail(), user.getRoles(), token);
    }

    protected abstract User findUser(String email);
    protected abstract void verifyPassword(String rawPassword, String hashedPassword);
    protected abstract String generateToken(User user);
}
