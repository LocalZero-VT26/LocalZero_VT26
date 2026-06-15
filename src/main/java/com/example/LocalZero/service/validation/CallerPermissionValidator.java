package com.example.LocalZero.service.validation;

import com.example.LocalZero.Model.Role;
import com.example.LocalZero.Model.User;
import com.example.LocalZero.dto.AssignRoleRequest;
import com.example.LocalZero.exception.ValidationException;

public class CallerPermissionValidator extends RoleAssignmentValidator{


    /**
     * The first filter in the CoR for validation.
     * Validates that if the user is not an admin or organizer, they cannot change roles of other users.
     */
    @Override
    protected void doValidate(User caller, User target, AssignRoleRequest request) {
        boolean isOrganizer = caller.getRoles().contains(Role.ORGANIZER);
        boolean isAdmin = caller.getRoles().contains(Role.ADMIN);

        if (!isOrganizer && !isAdmin) {
            throw new ValidationException("Only admins and organizers can change roles!");
        }
    }
}
