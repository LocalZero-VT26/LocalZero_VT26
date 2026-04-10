package Model;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String name;
    private List<Role> roles;

    // Constructor
    public User(String name, List<Role> roles) {
        this.name = name;
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


}