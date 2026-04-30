package com.example.LocalZero.service.registration;

import com.example.LocalZero.Model.User;
import com.example.LocalZero.dto.AuthResponse;
import com.example.LocalZero.dto.RegisterRequest;

public abstract class UserRegistrationTemplate {

    public final AuthResponse register(RegisterRequest request) {
        validateInput(request);
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setLocation(request.getLocation());
        hashAndSetPassword(user, request.getPassword());
        assignRole(user);
        User savedUser = saveUser(user);
        sendNotification(savedUser);
        String token = generateToken(savedUser);
        return new AuthResponse(savedUser.getId(), savedUser.getName(), savedUser.getEmail(), savedUser.getRoles(), token);
    }

    protected abstract void validateInput(RegisterRequest request);
    protected abstract void hashAndSetPassword(User user, String password);
    protected abstract void assignRole(User user);
    protected abstract User saveUser(User user);
    protected abstract String generateToken(User user);
    protected abstract void sendNotification(User user);
}
