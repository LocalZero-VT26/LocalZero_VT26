package com.example.LocalZero.service;

import com.example.LocalZero.dto.UserResponse;

import java.util.List;

public interface IUserService {
    UserResponse getUserById(Long id);
    List<UserResponse> getAllUsers();
}
