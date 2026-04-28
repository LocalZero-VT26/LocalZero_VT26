package com.example.LocalZero.controller;

import com.example.LocalZero.dto.AuthResponse;
import com.example.LocalZero.dto.LoginRequest;
import com.example.LocalZero.dto.RegisterRequest;
import com.example.LocalZero.service.IAuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final IAuthService authService;

    public AuthController(@Qualifier("authService") IAuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/logout")
    public void logout(@RequestHeader("Authorization") String authHeader) {
        authService.logout(authHeader.substring(7));
    }
}