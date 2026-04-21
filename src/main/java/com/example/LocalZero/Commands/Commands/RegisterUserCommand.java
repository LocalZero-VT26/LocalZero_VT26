package com.example.LocalZero.Commands.Commands;

import com.example.LocalZero.Model.User;
import com.example.LocalZero.Model.StandardUserRegistration;

public class RegisterUserCommand implements Command{

    private String name, email, location, password;
    private StandardUserRegistration registrationLogic = new StandardUserRegistration();

    public RegisterUserCommand(String name, String email, String location, String password) {
        this.name = name;
        this.email = email;
        this.location = location;
        this.password = password;
    }

    @Override
    public void execute(){
        registrationLogic.register(name, email, location, password);
    }

    @Override
    public User getUser() {
        return null;
    }

    @Override
    public CommandType getCommandType() {
        return CommandType.REGISTER_USER;
    }


}
