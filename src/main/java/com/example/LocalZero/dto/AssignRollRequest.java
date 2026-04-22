package com.example.LocalZero.dto;

import com.example.LocalZero.Model.Role;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssignRollRequest {

    @NotNull
    private Long userId;

    @NotNull
    private Role role;
}
