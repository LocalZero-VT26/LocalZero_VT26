package Commands.Logic;

import Commands.Commands.CreateInitiativeCommand;
import Commands.Commands.RegisterUserCommand;
import Model.User;
import Model.Role;

import java.util.ArrayList;
import java.util.List;

public class CommandTest {

    public static void main(String[] args) {

        PermissionChecker permissionChecker = new PermissionChecker();
        PermissionHandler permissionHandler = new PermissionHandler(permissionChecker);
        List<Role> roles = new ArrayList<>();
        roles.add(Role.RESIDENT);
        //roles.add(Role.ORGANIZER);
        RegisterUserCommand register = new RegisterUserCommand(
                "Alper",
                "alper@gmail.com",
                "Malmö",
                "lösenord123"
        );

        System.out.println("Starting registration");
        register.execute();

        User user = new User("Bob", "bob@gmail.com", "Malmö", "lösenord123", roles);

        CreateInitiativeCommand command = new CreateInitiativeCommand(user, "Hejsan", "jaja", "Malmö");

        ButtonInvoker button = new ButtonInvoker(command, permissionHandler);

        button.press();

    }
}