package com.example.LocalZero.controller;

import com.example.LocalZero.dto.AuthResponse;
import com.example.LocalZero.dto.DeleteAccountRequest;
import com.example.LocalZero.dto.LoginRequest;
import com.example.LocalZero.dto.RegisterRequest;
import com.example.LocalZero.service.IAuthService;
import com.example.LocalZero.service.OnlineUserRegistery;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final IAuthService authService;
    private final OnlineUserRegistery onlineUserRegistery;

    public AuthController(@Qualifier("authService") IAuthService authService,
                          OnlineUserRegistery onlineUserRegistery) {
        this.authService = authService;
        this.onlineUserRegistery = onlineUserRegistery;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        onlineUserRegistery.setOnline(response.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        onlineUserRegistery.setOnline(response.getEmail());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String authHeader,
                                       @RequestAttribute("email") String email) {
        onlineUserRegistery.setOffline(email);
        authService.logout(authHeader.substring(7));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/account")
    public ResponseEntity<Void> deleteAccount(@Valid @RequestBody DeleteAccountRequest request,
                                              @RequestHeader("Authorization") String authHeader,
                                              @RequestAttribute("email") String email) {
        onlineUserRegistery.setOffline(email);
        authService.deleteAccount(authHeader.substring(7), request);
        return ResponseEntity.noContent().build();
    }
}