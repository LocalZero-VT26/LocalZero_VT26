package com.example.LocalZero.service.validation;

import com.example.LocalZero.Model.Role;
import com.example.LocalZero.Model.User;
import com.example.LocalZero.dto.AssignRoleRequest;
import com.example.LocalZero.exception.ValidationException;

public class LocationMatchValidator extends RoleAssignmentValidator {


    /**
     * The second filter from CoR for validation.
     * If the users role is NOT an admin and is an organizer,
     * they can only change roles of users in the same location/neighborhood.
     */
    @Override
    protected void doValidate(User caller, User target, AssignRoleRequest request) {
        boolean isOrganizer = caller.getRoles().contains(Role.ORGANIZER);
        boolean isAdmin = caller.getRoles().contains(Role.ADMIN);

        if (isOrganizer && !isAdmin) {
            String callerLocation = caller.getLocation();
            String targetLocation = target.getLocation();

            if (callerLocation == null || targetLocation == null || !callerLocation.equals(targetLocation)) {
                throw new ValidationException("Organizers can only change roles for users in the same location!");
            }
        }

    }
}
