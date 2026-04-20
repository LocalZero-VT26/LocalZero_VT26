package com.example.LocalZero.service;

import com.example.LocalZero.dto.AssignRoleRequest;
import com.example.LocalZero.dto.UpdateProfileRequest;
import com.example.LocalZero.dto.UserResponse;

public interface IOrganizerService extends IUserService {
    UserResponse updateProfile(Long id, UpdateProfileRequest request, String callerEmail);
    void assignRole(AssignRoleRequest request, String callerEmail);
}
