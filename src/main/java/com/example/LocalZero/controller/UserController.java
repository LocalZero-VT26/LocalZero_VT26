package com.example.LocalZero.controller;

import com.example.LocalZero.dto.UserSummaryResponse;
import com.example.LocalZero.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.LocalZero.dto.AssignRoleRequest;

import java.util.List;

@RestController
@RequestMapping("/api/users/")
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;

    @GetMapping("/available")
    public ResponseEntity<List<UserSummaryResponse>> getAvailableUsers(@RequestAttribute("email") String email) {
        return ResponseEntity.ok(userService.getAvailableUsers(email));
    }

    @PutMapping("/assign-role")
    public ResponseEntity<Void> assignrole(@jakarta.validation.Valid @RequestBody AssignRoleRequest request,
                                           @RequestAttribute("email") String callerEmail){
        userService.assignRole(request, callerEmail);
        return ResponseEntity.ok().build();
    }
}
