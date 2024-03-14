package learning.websocket.dto;

import lombok.Getter;

@Getter
public class ChatMessageDto {

    public enum MessageType {
        ENTER, TALK
    }

    private MessageType messageType;
    private Long chatRoomId;
    private Long senderId;
    private String message;
}
