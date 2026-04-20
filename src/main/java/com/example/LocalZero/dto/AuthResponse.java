package com.example.LocalZero.dto;

import com.example.LocalZero.model.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;

@Getter
@AllArgsConstructor
public class AuthResponse {

    private String token;
    private String email;
    private Set<Role> roles;
}
