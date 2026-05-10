package com.example.LocalZero.service.notification;

import com.example.LocalZero.dto.NotificationData;
import com.example.LocalZero.service.INotification;
import com.example.LocalZero.service.INotificationSender;
import org.springframework.stereotype.Service;

@Service
public class AccountCreatedNotification implements INotification {

    private final INotificationSender sender;

    public AccountCreatedNotification(INotificationSender sender) {
        this.sender = sender;
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
        sender.send(data);
    }
}
