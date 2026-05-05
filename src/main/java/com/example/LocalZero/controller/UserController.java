package com.example.LocalZero.controller;

import com.example.LocalZero.dto.UserSummaryResponse;
import com.example.LocalZero.service.IUserService;
import com.example.LocalZero.service.OnlineUserRegistery;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;
    private final OnlineUserRegistery onlineUserRegistery;

    @GetMapping("/availabe")
    public ResponseEntity<List<UserSummaryResponse>> getAvailableUsers(@RequestAttribute("email") String email) {
        return ResponseEntity.ok(userService.getAvailableUsers(email));
    }

    @PostMapping("/online")
    public ResponseEntity<Void> setOnline(@RequestAttribute("email") String email) {
        onlineUserRegistery.setOnline(email);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/offline")
    public ResponseEntity<Void> setOffline(@RequestAttribute("email") String email) {
        onlineUserRegistery.setOffline(email);
        return ResponseEntity.ok().build();
    }
}
