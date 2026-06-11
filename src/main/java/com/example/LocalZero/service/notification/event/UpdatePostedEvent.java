package com.example.LocalZero.service.notification.event;

public record UpdatePostedEvent(
        Long initiativeId,
        String authorEmail,
        String authorName
) {}
