package com.example.LocalZero.Commands.Logic;

import com.example.LocalZero.Commands.Commands.Command;

public class ButtonInvoker {
    private Command command;
    private CommandHandler commandHandler;

    public ButtonInvoker(Command command, CommandHandler commandHandler) {
        this.command = command;
        this.commandHandler = commandHandler;
    }

    public void press() {
        commandHandler.canHandle(command);
    }
}
