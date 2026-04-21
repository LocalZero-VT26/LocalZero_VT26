package com.example.LocalZero.Commands.Logic;

import com.example.LocalZero.Commands.Commands.Command;
import com.example.LocalZero.Commands.Commands.CommandType;
import com.example.LocalZero.Model.Role;
import com.example.LocalZero.Model.User;

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
            System.out.println("Not allowed: no roles configured for this command");
            return false;
        }

        boolean result = user.getRoles().stream().anyMatch(rolesAllowed::contains);

        if(result)
        {
            System.out.println("Allowed");
        }
        else
        {
            System.out.println("Not allowed");
        }

        return result;
    }


}
