package com.example.LocalZero.service;

import com.example.LocalZero.dto.NotificationData;

import java.util.List;

public interface IEmailService {

    void send(NotificationData data);
    void send(List<NotificationData> dataList);
}
