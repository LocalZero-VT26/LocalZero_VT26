package com.example.LocalZero.service.sustainability;

import com.example.LocalZero.Model.User;
import com.example.LocalZero.exception.ResourceNotFoundException;

public class UserValidator extends EcoValidator {

    /**
     * The first filter in the CoR-pattern for EcoValidation, which checks that there
     * is a valid user for the eco-action.
     */

    @Override
    public void validate(String description, User user) {
        if (user == null) {
            throw new ResourceNotFoundException("User could not be found for the eco-action!");
        }

        if (next != null) {
            next.validate(description, user);
        }
    }
}
