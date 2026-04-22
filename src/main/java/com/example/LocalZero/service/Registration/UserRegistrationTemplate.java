package com.example.LocalZero.service.Registration;

import com.example.LocalZero.Model.User;
import com.example.LocalZero.dto.RegisterRequest;

public abstract class UserRegistrationTemplate {

    public final User register(RegisterRequest request){
        validateInput(request);
        User user = new User();

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setLocation(request.getLocation());

        hashAndSetPassword(user, request.getPassword());
        assignRoll(user);
        return saveUser(user);
    }

    protected abstract void validateInput(RegisterRequest request);
    protected abstract void hashAndSetPassword(User user, String password);
    protected abstract void assignRoll(User user);
    protected abstract User saveUser(User user);
}
