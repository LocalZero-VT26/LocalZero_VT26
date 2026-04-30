package com.example.LocalZero.service.notification;

import com.example.LocalZero.dto.NotificationData;
import com.example.LocalZero.service.IAccountDeletedNotification;
import com.example.LocalZero.service.IEmailService;
import org.springframework.stereotype.Service;

@Service
public class AccountDeleteNotification implements IAccountDeletedNotification {

    private final IEmailService emailService;

    public AccountDeleteNotification(IEmailService emailService) {
        this.emailService = emailService;
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
        emailService.send(data);
    }
}
