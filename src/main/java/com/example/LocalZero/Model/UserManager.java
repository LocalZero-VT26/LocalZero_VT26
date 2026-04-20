package com.example.LocalZero.Model;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

public class UserManager {

    private static final UserManager instance = new UserManager();

    private List<User> users = new ArrayList<>();

    private UserManager() {} // Privat konstruktor för Singleton

    public static UserManager getInstance() {
        return instance;
    }

    public void addUser(User user) {
        users.add(user);
    }

    public List<User> getUsers() {
        return Collections.unmodifiableList(users);
    }

}
