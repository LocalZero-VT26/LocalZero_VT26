package com.example.LocalZero.service.impl;

import com.example.LocalZero.dto.NotificationData;
import com.example.LocalZero.service.IEmailService;
import com.example.LocalZero.service.INotificationSender;
import org.springframework.stereotype.Service;

@Service
public class EmailNotificationSender implements INotificationSender {

    private final IEmailService emailService;

    public EmailNotificationSender(IEmailService emailService) {
        this.emailService = emailService;
    }

    @Override
    public void send(NotificationData data) {
        emailService.send(data);
    }
}
