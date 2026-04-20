package com.example.LocalZero.service;

import com.example.LocalZero.dto.AuthResponse;
import com.example.LocalZero.dto.ChangePasswordRequest;
import com.example.LocalZero.dto.DeleteAccountRequest;
import com.example.LocalZero.dto.LoginRequest;
import com.example.LocalZero.dto.RegisterRequest;

public interface IAuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
    void changePassword(String email, ChangePasswordRequest request);
    void deleteAccount(String email, DeleteAccountRequest request);
}
