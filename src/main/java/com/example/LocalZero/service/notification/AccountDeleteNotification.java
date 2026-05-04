package com.example.LocalZero.service.notification;

import com.example.LocalZero.dto.NotificationData;
import com.example.LocalZero.service.INotification;
import com.example.LocalZero.service.INotificationSender;
import org.springframework.stereotype.Service;

@Service
public class AccountDeleteNotification implements INotification {

    private final INotificationSender sender;

    public AccountDeleteNotification(INotificationSender sender) {
        this.sender = sender;
    }

    @Override
    public void notify(String to, String name) {
        NotificationData data = new NotificationData(
                to,
                name,
                "",
                "Account Deleted",
                "Your LocalZero account has been deleted."
        );
        sender.send(data);
    }
}
