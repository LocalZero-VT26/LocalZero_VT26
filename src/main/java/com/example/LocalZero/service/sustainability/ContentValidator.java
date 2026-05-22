package com.example.LocalZero.service.sustainability;

import com.example.LocalZero.Model.User;
import com.example.LocalZero.exception.ValidationException;

public class ContentValidator extends EcoValidator{

    @Override
    public void validate(String description, User user) {
        if(description == null || description.trim().isEmpty()) {
            throw new ValidationException("Description cannot be empty");
        }

        if (user != null) {
            next.validate(description, user);
        }
    }
}
