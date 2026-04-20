package com.example.LocalZero.service;

import com.example.LocalZero.dto.UpdateProfileRequest;
import com.example.LocalZero.dto.UserResponse;

public interface IResidentService extends IUserService {
    UserResponse updateProfile(Long id, UpdateProfileRequest request, String callerEmail);
}
