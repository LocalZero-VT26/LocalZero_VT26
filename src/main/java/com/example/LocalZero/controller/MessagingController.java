package com.example.LocalZero.controller;

import com.example.LocalZero.dto.ChatMessageResponse;
import com.example.LocalZero.dto.ChatRoomSummaryResponse;
import com.example.LocalZero.dto.SendMessageRequest;
import com.example.LocalZero.service.IChatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messaging")
@RequiredArgsConstructor
public class MessagingController {

    private final IChatService chatService;

    @PostMapping("/send")
    public ResponseEntity<Void> sendMessage(@Valid @RequestBody SendMessageRequest request,
                                            @RequestAttribute("email") String email) {
        chatService.sendMessage(email, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/rooms")
    public ResponseEntity<List<ChatRoomSummaryResponse>> getRooms(@RequestAttribute("email") String email) {
        return ResponseEntity.ok(chatService.getRoomsForUser(email));
    }

    @GetMapping("/rooms/{roomId}")
    public ResponseEntity<List<ChatMessageResponse>> getRoomMessages(@PathVariable Long roomId,
                                                                     @RequestAttribute("email") String email) {
        return ResponseEntity.ok(chatService.getRoomMessage(email, roomId));
    }


}
