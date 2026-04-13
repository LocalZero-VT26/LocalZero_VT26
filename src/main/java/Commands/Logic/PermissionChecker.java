package Commands.Logic;

import Commands.Commands.Command;
import Commands.Commands.CommandType;
import Model.Role;
import Model.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PermissionChecker {
    private Map<CommandType, Set<Role>> permissions = new HashMap<>();

    public PermissionChecker()
    {
        permissions.put(CommandType.CREATE_INITIATIVE, Set.of(Role.OWNER, Role.ORGANIZER));
    }


    public boolean isAllowed(User user, Command command)
    {
        Set<Role> rolesAllowed = permissions.get(command.getCommandType());

        if(rolesAllowed == null)
        {
            return false;
        }

        return user.getRoles().stream().anyMatch(rolesAllowed::contains);
    }


}
