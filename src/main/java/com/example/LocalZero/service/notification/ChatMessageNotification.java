package com.example.LocalZero.service.notification;

import com.example.LocalZero.dto.NotificationData;
import com.example.LocalZero.service.INotification;
import com.example.LocalZero.service.INotificationSender;
import org.springframework.stereotype.Service;

@Service
public class ChatMessageNotification implements INotification {

    private INotificationSender sender;

    public ChatMessageNotification(INotificationSender sender) {
        this.sender = sender;
    }

    @Override
    public void notify(String to, String name) {
        NotificationData data = new NotificationData(
                to,
                name,
                "",
                "New message from " + name,
                "You have received a new message"
        );

        sender.send(data);
    }
}
