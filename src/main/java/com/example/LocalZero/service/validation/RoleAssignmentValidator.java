package com.example.LocalZero.service.validation;

import com.example.LocalZero.Model.User;
import com.example.LocalZero.dto.AssignRoleRequest;

public class RoleAssignmentValidator {

    protected RoleAssignmentValidator next;

    public void setNext(RoleAssignmentValidator next) {
        this.next = next;
    }


    public void validate(User caller, User target, AssignRoleRequest request){
        doValidate(caller, target, request);
        if(next != null){
            next.validate(caller, target, request);
        }
    }

    protected abstract void doValidate(User caller, User target, AssignRoleRequest request);

}
