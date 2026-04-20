package com.example.LocalZero.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;

@Getter
@Data
public class LoginRequest {

    @NotBlank @Email
    private String email;

    @NotBlank
    private String password;
}
