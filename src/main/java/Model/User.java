package Model;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String name;
    private String password;
    private String email;
    private String location;
    private List<Role> roles;

    // Constructor
    public User(String name, String email, String location, String password, List<Role> roles) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.location = location;
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