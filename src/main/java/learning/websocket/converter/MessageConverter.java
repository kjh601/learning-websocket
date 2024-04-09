package learning.websocket.converter;

import learning.websocket.dto.MessageDto;
import learning.websocket.entity.Message;
import learning.websocket.entity.Room;
import learning.websocket.entity.User;
import org.springframework.stereotype.Component;


@Component
public class MessageConverter {

    public MessageDto.logRes toLogRes(Message message, Long senderId) {
        return MessageDto.logRes.builder()
                .senderName(message.getUser().getName())
                .messageType(message.getMessageType().toString())
                .createAt(message.getCreateAt())
                .isMine(message.getUser().getId().equals(senderId))
                .message(message.getContent())
                .build();
    }

    public Message toMessage(MessageDto.saveReq saveReq, User user, Room room) {
        return Message.builder()
                .user(user)
                .room(room)
                .content(saveReq.getMessage())
                .messageType(saveReq.getMessageType())
                .build();
    }
}
