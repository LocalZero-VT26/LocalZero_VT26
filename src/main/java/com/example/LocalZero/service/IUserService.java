package com.example.LocalZero.service;

import com.example.LocalZero.dto.UserSummaryResponse;
import java.util.List;

public interface IUserService {
    List<UserSummaryResponse> getAvailableUsers(String userEmail);
}