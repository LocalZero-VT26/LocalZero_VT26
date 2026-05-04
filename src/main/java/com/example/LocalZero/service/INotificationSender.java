package com.example.LocalZero.service;

import com.example.LocalZero.dto.NotificationData;

public interface INotificationSender {
    void send(NotificationData data);
}
