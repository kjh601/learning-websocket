package learning.websocket.service;

import jakarta.transaction.Transactional;
import learning.websocket.converter.MessageConverter;
import learning.websocket.dto.MessageDto;
import learning.websocket.entity.Message;
import learning.websocket.entity.Room;
import learning.websocket.entity.User;
import learning.websocket.enums.MessageType;
import learning.websocket.repository.MessageRepository;
import learning.websocket.repository.RoomRepository;
import learning.websocket.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class MySQLMessageServiceImpl implements MessageService{

    private final MessageRepository messageRepository;
    private final MessageConverter messageConverter;

    private final UserRepository userRepository;
    private final RoomRepository roomRepository;

    private final SimpMessagingTemplate template;

    @Override
    public void save(MessageDto.saveReq saveReq, Long roomId) {

        User user = userRepository.findById(saveReq.getSenderId()).orElseThrow();
        Room room = roomRepository.findById(roomId).orElseThrow();

        Message message = messageConverter.toMessage(saveReq, user, room);

        messageRepository.save(message);
    }

    @Override
    @Transactional
    public List<MessageDto.logRes> getMessages(Long roomId, Long userId) {
        Room findRoom = roomRepository.findById(roomId).orElseThrow();

        return messageRepository.findAllByRoom(findRoom).stream()
                .map(room -> messageConverter.toLogRes(room, userId))
                .collect(Collectors.toList());
    }

    @Override
    public void sendMessage(Long userId, Long roomId, MessageType messageType) {
        User user = userRepository.findById(userId).orElseThrow();
        Room room = roomRepository.findById(roomId).orElseThrow();

        String destination = "/sub/rooms/" + roomId;

        String subString = messageType.equals(MessageType.ENTER) ? "씨가 입장하셨습니다." : "씨가 퇴장하셨습니다.";

        MessageDto.saveReq notification = MessageDto.saveReq.builder()
                .messageType(messageType)
                .senderId(userId)
                .senderName(user.getName())
                .message(user.getName()+subString)
                .build();

        template.convertAndSend(destination, notification);

        Message message = messageConverter.toMessage(notification, user, room);

        messageRepository.save(message);
    }
}
