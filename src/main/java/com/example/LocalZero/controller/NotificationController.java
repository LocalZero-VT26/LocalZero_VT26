package com.example.LocalZero.controller;

import com.example.LocalZero.dto.NotificationResponse;
import com.example.LocalZero.service.INotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST Controller for in-app notifications.
 * Lists a user's notifications, exposes the unread count for the
 * notification bell, and lets the user mark notifications as read.
 */
@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final INotificationService notificationService;

    @GetMapping
    public List<NotificationResponse> getNotifications(@RequestAttribute("email") String email) {
        return notificationService.getNotifications(email);
    }

    @GetMapping("/unread-count")
    public Map<String, Long> getUnreadCount(@RequestAttribute("email") String email) {
        return Map.of("count", notificationService.getUnreadCount(email));
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<Void> markRead(@PathVariable Long id,
                                         @RequestAttribute("email") String email) {
        notificationService.markRead(id, email);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/read-all")
    public ResponseEntity<Void> markAllRead(@RequestAttribute("email") String email) {
        notificationService.markAllRead(email);
        return ResponseEntity.ok().build();
    }
}
