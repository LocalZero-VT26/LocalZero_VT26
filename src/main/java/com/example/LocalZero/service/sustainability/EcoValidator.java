package com.example.LocalZero.service.sustainability;

import com.example.LocalZero.Model.User;

/**
 * An abstract class that handles EcoValidation.
 * This is the base-filter, and is instantiated in
 * SustainabilityServiceImpl, where the chain of responsibility is built.
 */

public abstract class EcoValidator {

    protected EcoValidator next;

    public void setNext(EcoValidator next) {
        this.next = next;
    }

    public abstract void validate(String description, User user);

}
