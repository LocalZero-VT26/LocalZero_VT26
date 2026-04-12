package Commands.Logic;

import Commands.Commands.Command;

public class ButtonInvoker {
    private Command command;

    public ButtonInvoker(Command command) {
        this.command = command;
    }

    public void press() {
        command.execute();
    }
}
