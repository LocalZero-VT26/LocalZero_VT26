package com.example.LocalZero.dto;

import com.example.LocalZero.model.Role;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AssignRoleRequest {

    @NotNull
    private Long userId;

    @NotNull
    private Role role;
}
