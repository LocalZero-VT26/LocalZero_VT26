package com.example.LocalZero.Model;

import java.util.List;

public class StandardUserRegistration extends UserRegistrationTemplate {
    @Override
    protected void validate(String email, String password) {
        if (!email.contains("@")) throw new RuntimeException("Felaktig e-post");
    }

    @Override
    protected User createUser(String n, String e, String l, String p) {
        return new User(n, e, l, p, List.of(Role.RESIDENT));
    }
}
