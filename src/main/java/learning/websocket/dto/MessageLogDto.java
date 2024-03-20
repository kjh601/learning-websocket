package learning.websocket.dto;

import learning.websocket.enums.MessageType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class MessageLogDto {
        private MessageType messageType;
        private Long chatRoomId;
        private Long senderId;
        private LocalDateTime createAt;
        private String message;
}
