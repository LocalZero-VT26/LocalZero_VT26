package com.example.LocalZero.service.sustainability;

import com.example.LocalZero.Model.User;

public abstract class EcoValidator {

    protected EcoValidator next;

    public void setNext(EcoValidator next) {
        this.next = next;
    }

    public abstract void validate(String description, User user);

}
