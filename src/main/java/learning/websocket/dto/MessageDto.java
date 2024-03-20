package learning.websocket.dto;

import learning.websocket.enums.MessageType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MessageDto {
    private MessageType messageType;
    private Long chatRoomId;
    private Long senderId;
    private String message;
}
