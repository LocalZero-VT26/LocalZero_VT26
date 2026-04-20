package com.example.LocalZero.Service.Registration;

import com.example.LocalZero.dto.RegisterRequest;
import com.example.LocalZero.model.User;

public abstract class UserRegistrationTemplate implements IRegistrationService {

    public final User register(RegisterRequest request) {
        validateInput(request);

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setLocation(request.getLocation());

        hashAndSetPassword(user, request.getPassword());
        assignRole(user);
        //send notification email
        return saveUser(user);
    }

    protected abstract void validateInput(RegisterRequest request);
    protected abstract void hashAndSetPassword(User user, String password);
    protected abstract void assignRole(User user);
    protected abstract User saveUser(User user);

    //send notification email
}
