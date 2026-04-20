package com.example.LocalZero.dto;

import com.example.LocalZero.model.Role;
import com.example.LocalZero.model.User;
import lombok.Getter;

import java.util.Set;

@Getter
public class UserResponse {

    private long id;
    private String name;
    private String email;
    private String location;
    private Set<Role> roles;

    public UserResponse(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.location = user.getLocation();
        this.roles = user.getRoles();
    }
}
