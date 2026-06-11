package com.example.LocalZero.service;

import com.example.LocalZero.dto.NotificationResponse;

import java.util.List;

public interface INotificationService {

    List<NotificationResponse> getNotifications(String userEmail);

    long getUnreadCount(String userEmail);

    void markRead(Long notificationId, String userEmail);

    void markAllRead(String userEmail);
}
