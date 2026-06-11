package com.example.LocalZero.service.notification.event;

/**
 * Domain event published when a chat message is sent.
 * Consumed by listeners (Observer pattern) that create an
 * in-app notification for the recipient.
 */
public record NewChatMessageEvent(String recipientEmail, String senderName) {
}
