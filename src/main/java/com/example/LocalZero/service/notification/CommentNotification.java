package com.example.LocalZero.service.notification;

import com.example.LocalZero.dto.NotificationData;
import com.example.LocalZero.service.INotification;
import com.example.LocalZero.service.INotificationSender;
import org.springframework.stereotype.Service;

@Service("commentNotification")
public class CommentNotification implements INotification {

    private final INotificationSender sender;

    public CommentNotification(INotificationSender sender) {
        this.sender = sender;
    }

    @Override
    public void notify(String to, String name) {
        NotificationData data = new NotificationData(
                to,
                name,
                "",
                "New comment on your update",
                name + " commented on your update"
        );
        sender.send(data);
    }
}
