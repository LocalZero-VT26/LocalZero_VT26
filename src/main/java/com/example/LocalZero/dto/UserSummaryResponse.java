package com.example.LocalZero.dto;


import com.example.LocalZero.Model.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class UserSummaryResponse {

    private Long id;
    private String name;
    private String location;
    private List<Role> roles;
    private String email;
    private boolean online;

    public UserSummaryResponse(Long id, String name, String location, List<Role> roles, String email, boolean online) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.roles = roles;
        this.email = email;
        this.online = online;
    }
}