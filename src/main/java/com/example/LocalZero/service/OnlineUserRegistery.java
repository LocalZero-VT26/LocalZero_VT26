package com.example.LocalZero.service;

import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Component
public class OnlineUserRegistery {

    private final Set<String> onlineUsers = Collections.synchronizedSet(new HashSet<>());

    public void setOnline(String email) {
        onlineUsers.add(email);
    }

    public void setOffline(String email) {
        onlineUsers.remove(email);
    }

    public boolean isOnline(String email) {
        return onlineUsers.contains(email);
    }
}
