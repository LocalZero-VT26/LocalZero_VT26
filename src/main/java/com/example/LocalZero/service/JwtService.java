package com.example.LocalZero.service;

import com.example.LocalZero.Model.Role;

import java.util.Date;
import java.util.List;

public interface JwtService {
    String generateToken(String email, List<Role> roles);
    String extractEmail(String token);
    List<String> extractRoles(String token);
    boolean isTokenValid(String token);
    Date extractExpiration(String token);
}
