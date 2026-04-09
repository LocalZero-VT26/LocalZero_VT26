package Model;

import java.util.List;

public class User {
    private String name;
    private List[] roles;

    // Constructor
    public User(String name, List[] roles) {
        this.name = name;
        this.roles = roles;
    }

    // Getter for name
    public String getName() {
        return name;
    }

    // Getter for roles
    public List[] getRoles() {
        return roles;
    }


}