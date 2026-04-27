package com.example.LocalZero.dto;

import com.example.LocalZero.Model.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class AuthResponse {

    private Long id;
    private String name;
    private String email;
    private List<Role> roles;
    private String token;
}
