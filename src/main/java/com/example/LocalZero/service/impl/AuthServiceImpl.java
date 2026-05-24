package com.example.LocalZero.service.impl;

import com.example.LocalZero.dto.AuthResponse;
import com.example.LocalZero.dto.DeleteAccountRequest;
import com.example.LocalZero.dto.LoginRequest;
import com.example.LocalZero.dto.RegisterRequest;
import com.example.LocalZero.service.IAuthService;
import com.example.LocalZero.service.deleteaccount.UserDeleteAccountTemplate;
import com.example.LocalZero.service.login.UserLoginTemplate;
import com.example.LocalZero.service.logout.UserLogoutTemplate;
import com.example.LocalZero.service.registration.UserRegistrationTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("authService")
public class AuthServiceImpl implements IAuthService {

    private final UserRegistrationTemplate registrationTemplate;
    private final UserLoginTemplate loginTemplate;
    private final UserLogoutTemplate logoutTemplate;
    private final UserDeleteAccountTemplate userDeleteAccountTemplate;

    public AuthServiceImpl(
            @Qualifier("userRegistration") UserRegistrationTemplate registrationTemplate,
            @Qualifier("userLogin") UserLoginTemplate loginTemplate,
            @Qualifier("userLogout") UserLogoutTemplate logoutTemplate,
            @Qualifier("userDeleteAccount") UserDeleteAccountTemplate deleteAccountTemplate) {

        this.registrationTemplate = registrationTemplate;
        this.loginTemplate = loginTemplate;
        this.logoutTemplate = logoutTemplate;
        this.userDeleteAccountTemplate = deleteAccountTemplate;
    }

    @Override
    public AuthResponse register(RegisterRequest request) {
        return registrationTemplate.register(request);
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        return loginTemplate.login(request);
    }

    @Override
    public void logout(String token) {
        logoutTemplate.logout(token);
    }

    @Override
    public void deleteAccount(String token, DeleteAccountRequest request) {
        userDeleteAccountTemplate.deleteAccount(token, request);
    }
}
