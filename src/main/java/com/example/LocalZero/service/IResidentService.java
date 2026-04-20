package com.example.LocalZero.service;

import com.example.LocalZero.dto.RegisterRequest;
import com.example.LocalZero.dto.UserResponse;

public interface IResidentService extends IUserService {
    UserResponse updateProfile(Long id, RegisterRequest request, String callerEmail);
}
