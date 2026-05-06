package com.example.LocalZero.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SendMessageRequest {

    @NotBlank(message = "Recipient email cannot be blank")
    @Email(message = "Recipient email must be a valid email address")
    private String recipientEmail;

    @NotBlank(message = "Content cannot be blank")
    private String content;
}