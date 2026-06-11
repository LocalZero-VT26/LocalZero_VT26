package com.example.LocalZero.service.validation;

import com.example.LocalZero.Model.Role;
import com.example.LocalZero.Model.User;
import com.example.LocalZero.dto.AssignRoleRequest;
import com.example.LocalZero.exception.ValidationException;

public class RoleTransitionValidator extends RoleAssignmentValidator {

    @Override
    protected void doValidate(User caller, User target, AssignRoleRequest request) {
        boolean isAdmin = caller.getRoles().contains(Role.ADMIN);
        Role requestedRole = request.getRole();

        if (requestedRole == Role.ADMIN && !isAdmin) {
            throw new ValidationException("Only admins can assign the admin role!");
        }

        boolean targetIsOrganizer = target.getRoles().contains(Role.ORGANIZER);
        if (targetIsOrganizer && requestedRole == Role.RESIDENT && !isAdmin) {
            throw new ValidationException("Only admins can demote organizers to residents!");
        }
    }
}
