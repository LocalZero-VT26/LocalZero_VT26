package com.example.LocalZero.service.impl;

import com.example.LocalZero.Model.User;
import com.example.LocalZero.dto.AuthResponse;
import com.example.LocalZero.dto.RegisterRequest;
import com.example.LocalZero.service.IAuthService;
import com.example.LocalZero.service.Registration.UserRegistrationTemplate;
import org.springframework.stereotype.Service;

@Service
public class AuthResidentImpl implements IAuthService {

    private final UserRegistrationTemplate registrationTemplate;

    public AuthResidentImpl(UserRegistrationTemplate userRegistrationTemplate) {
        this.registrationTemplate = userRegistrationTemplate;
    }

    @Override
    public AuthResponse register(RegisterRequest request) {
        User user = registrationTemplate.register(request);
        return new AuthResponse(user.getId(), user.getName(), user.getEmail(), user.getRoles());
    }
}
