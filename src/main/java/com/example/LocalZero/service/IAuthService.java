package com.example.LocalZero.service;

import com.example.LocalZero.dto.AuthResponse;
import com.example.LocalZero.dto.RegisterRequest;

public interface IAuthService {
    AuthResponse register(RegisterRequest request);
}
