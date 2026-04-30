package com.example.LocalZero.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationData {

    private String to;
    private String senderName;
    private String location;
    private String subject;
    private String description;
}
