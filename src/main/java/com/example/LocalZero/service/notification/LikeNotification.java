package com.example.LocalZero.service.notification;

import com.example.LocalZero.dto.NotificationData;
import com.example.LocalZero.service.INotification;
import com.example.LocalZero.service.INotificationSender;
import org.springframework.stereotype.Service;

@Service("likeNotification")
public class LikeNotification implements INotification {

    private final INotificationSender sender;

    public LikeNotification(INotificationSender sender) {
        this.sender = sender;
    }

    @Override
    public void notify(String to, String name) {
        NotificationData data = new NotificationData(
                to,
                name,
                "",
                "Someone liked your update",
                name + " liked your update"
        );
        sender.send(data);
    }
}
