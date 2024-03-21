package learning.websocket.dto;

import learning.websocket.enums.MessageType;
import lombok.Getter;

@Getter
public class MessageDto {
    private MessageType messageType;
    private Long chatRoomId;
    private Long senderId;
    private String message;
}
