package Commands.Logic;

import Commands.Commands.CreateInitiativeCommand;
import Model.User;
import Model.Role;

import java.util.ArrayList;
import java.util.List;

public class CommandTest {

    public static void main(String[] args) {

        List<Role> roles = new ArrayList<>();
        roles.add(Role.OWNER);

        User user = new User("Bob", roles);

        CreateInitiativeCommand command =
                new CreateInitiativeCommand(user, "Hejsan", "jaja", "Malmö");

        ButtonInvoker button = new ButtonInvoker(command);

        button.press();

    }
}