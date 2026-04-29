package com.example.LocalZero.service.notification;

import com.example.LocalZero.dto.NotificationData;
import com.example.LocalZero.service.IAccountCreatedNotification;
import com.example.LocalZero.service.IEmailService;
import org.springframework.stereotype.Service;

@Service
public class AccountCreatedNotification implements IAccountCreatedNotification {

    private final IEmailService emailService;

    public AccountCreatedNotification(IEmailService emailService) {
        this.emailService = emailService;
    }

    @Override
    public void notify(String to, String name) {
        NotificationData data = new NotificationData(
                to,
                name,
                "",
                "Welcome to LocalZero!",
                "Your Account has been created successfully."
        );
        emailService.send(data);
    }
}
