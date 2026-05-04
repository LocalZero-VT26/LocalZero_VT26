package com.example.LocalZero.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SendMessageRequest {

    @NotBlank(message = "Recipient email cannot be blank")
    private String recipientEmail;

    @NotBlank(message = "Content cannot be blank")
    private String content;
}