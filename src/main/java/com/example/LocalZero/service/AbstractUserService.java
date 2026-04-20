package com.example.LocalZero.service;

import com.example.LocalZero.dto.UserResponse;
import com.example.LocalZero.model.User;
import com.example.LocalZero.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractUserService {

    protected final UserRepository userRepository;

    protected AbstractUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found: " + id));
        return new UserResponse(user);
    }

    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserResponse> result = new ArrayList<>();
        for (User user : users) {
            result.add(new UserResponse(user));
        }
        return result;
    }
}
