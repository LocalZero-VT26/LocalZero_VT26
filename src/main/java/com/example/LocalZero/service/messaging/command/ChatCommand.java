package com.example.LocalZero.service.messaging.command;

import com.example.LocalZero.dto.ChatMessageResponse;


public interface ChatCommand {
    ChatMessageResponse execute();
}
