package com.example.LocalZero.service.impl;

import com.example.LocalZero.dto.NotificationData;
import com.example.LocalZero.service.IEmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailServiceImpl implements IEmailService {

    private final JavaMailSender mailSender;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void send(NotificationData data) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(data.getTo());
            helper.setSubject(data.getSubject());
            helper.setText(buildHtml(data), true);
            mailSender.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }

    @Override
    public void send(List<NotificationData> dataList) {
        for (NotificationData data : dataList) {
            send(data);
        }
    }

    private String buildHtml(NotificationData data) {
        return """
                  <html>
                  <body style="font-family: Arial, sans-serif; padding: 20px;">
                      <h2>%s</h2>
                      <p><strong>%s</strong> %s</p>
                  </body>
                  </html>
                  """.formatted(data.getSubject(), data.getSenderName(), data.getDescription());
    }
}
