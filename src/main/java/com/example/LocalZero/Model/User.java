package com.example.LocalZero.Model;

import java.util.List;
import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String password;
    private String email;
    private String location;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private List<Role> roles;


    public User() {}

    // Constructor
    public User(String name, String email, String location, String password, List<Role> roles) {
            this.name = name;
            this.email = email;
            this.location = location;
            this.password = password;
            this.roles = roles;
    }

    // Getter for name
    public String getName() {
        return name;
    }

    // Getter for roles
    public List<Role> getRoles() {
        return roles;
    }

    public boolean hasRole(Role role) {
        return roles.contains(role);
    }

    public String getEmail() {
        return email;
    }

    public String getLocation() {
        return location;
    }

    public String getPassword() {
        return password;
    }



}