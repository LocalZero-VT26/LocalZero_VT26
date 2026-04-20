package com.example.LocalZero.Commands.Commands;

import com.example.LocalZero.Model.User;

public interface Command {
    void execute();
    User getUser();
    CommandType getCommandType();
}
