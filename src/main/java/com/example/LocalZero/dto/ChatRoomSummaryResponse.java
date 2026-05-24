package com.example.LocalZero.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ChatRoomSummaryResponse {

    private Long roomId;
    private String otherUserEmail;
    private String otherUserName;
    private boolean hasUnseenMessages;
    private boolean otherUserOnline;

    public ChatRoomSummaryResponse(Long roomId, String otherUserEmail, String otherUserName, boolean hasUnseenMessages, boolean otherUserOnline) {
        this.roomId = roomId;
        this.otherUserEmail = otherUserEmail;
        this.otherUserName = otherUserName;
        this.hasUnseenMessages = hasUnseenMessages;
        this.otherUserOnline = otherUserOnline;
    }
}