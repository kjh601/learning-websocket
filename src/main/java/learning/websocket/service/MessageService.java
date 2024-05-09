package learning.websocket.service;

import learning.websocket.dto.MessageDto;
import learning.websocket.enums.MessageType;

import java.util.List;

public interface MessageService {

    void save(MessageDto.saveReq saveReq, Long roomId);

    List<MessageDto.logRes> getMessages(Long roomId, Long userId);

    void sendMessage(Long userId, Long roomId, MessageType messageType);
}
