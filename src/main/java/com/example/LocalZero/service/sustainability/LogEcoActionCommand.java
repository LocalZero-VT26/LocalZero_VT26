package com.example.LocalZero.service.sustainability;

import com.example.LocalZero.Model.EcoAction;
import com.example.LocalZero.Model.User;
import com.example.LocalZero.repository.EcoActionRepository;

public class LogEcoActionCommand implements IEcoCommand {

    private final String description;
    private final User user;
    private final EcoActionRepository repository;


    public LogEcoActionCommand(String description, User user, EcoActionRepository repository) {
        this.description = description;
        this.user = user;
        this.repository = repository;
    }

    /**
     * Part of the command pattern, this method creates a new EcoAction with the provided description and user and saving
     * them in the database.
     */

    @Override
    public void execute() {
        EcoAction action = new EcoAction(description, user);
        repository.save(action);
    }
}
