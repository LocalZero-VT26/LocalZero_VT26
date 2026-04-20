package com.example.LocalZero.Service.Registration;

import com.example.LocalZero.dto.RegisterRequest;
import com.example.LocalZero.model.User;

public interface IRegistrationService {

    User register(RegisterRequest request);
}
