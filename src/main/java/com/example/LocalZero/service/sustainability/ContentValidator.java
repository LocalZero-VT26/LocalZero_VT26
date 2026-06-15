package com.example.LocalZero.service.sustainability;

import com.example.LocalZero.Model.User;
import com.example.LocalZero.exception.ValidationException;

public class ContentValidator extends EcoValidator{

    /**
     * This is the second and final validator for the EcoValidation.
     * This makes sure that the description of the eco-action is not empty or null.
     */

    @Override
    public void validate(String description, User user) {
        if(description == null || description.trim().isEmpty()) {
            throw new ValidationException("Description cannot be empty");
        }

        if (next != null) {
            next.validate(description, user);
        }
    }
}
