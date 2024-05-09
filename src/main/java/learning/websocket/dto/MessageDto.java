package learning.websocket.dto;

import learning.websocket.enums.MessageType;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

public class MessageDto {

    @Getter
    @Builder
    @ToString
    public static class saveReq{
        private MessageType messageType;
        private Long senderId;
        private String senderName;
        private String message;
    }

    @Getter
    @Builder
    @ToString
    public static class logRes{
        private String messageType;
        private String  senderName;
        private Boolean isMine;
        private LocalDateTime createAt;
        private String message;
    }
}
