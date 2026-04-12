package Commands.Logic;

import Commands.Commands.Command;

public abstract class CommandHandler {
    protected  CommandHandler next;

    public void setNext(CommandHandler next)
    {
        this.next = next;
    }

    public void handle(Command command)
    {
        if(canHandle(command))
        {
            process(command);
        }

        else if(next != null)
        {
            next.handle(command);
        }

        else {
            throw new RuntimeException("No authority to execute command");
        }
    }



    protected abstract void process(Command command);




    protected abstract boolean canHandle(Command command);
}
