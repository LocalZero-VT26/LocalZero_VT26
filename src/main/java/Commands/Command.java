package Commands;

import Model.User;

public interface Command {
    void execute();
    User getUser();
    CommandType getCommandType();
}
