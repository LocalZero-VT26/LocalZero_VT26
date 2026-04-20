package com.example.LocalZero.service;

import com.example.LocalZero.dto.AssignRoleRequest;
import com.example.LocalZero.dto.RegisterRequest;
import com.example.LocalZero.dto.UserResponse;

public interface IOrganizerService extends IUserService {
    UserResponse updateProfile(Long id, RegisterRequest request, String callerEmail);
    void assignRole(AssignRoleRequest request, String callerEmail);
}
