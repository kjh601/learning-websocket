package learning.websocket.service;

import learning.websocket.dto.MessageDto;
import learning.websocket.entity.Message;
import learning.websocket.entity.Room;
import learning.websocket.entity.User;
import learning.websocket.entity.UserRoom;
import learning.websocket.repository.MessageRepository;
import learning.websocket.repository.RoomRepository;
import learning.websocket.repository.UserRepository;
import learning.websocket.repository.UserRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserRoomRepository userRoomRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;

    public void save(MessageDto messageDto, Long roomId) {

        User user = userRepository.findById(messageDto.getSenderId()).orElseThrow();
        Room room = roomRepository.findById(roomId).orElseThrow();
        UserRoom userRoom = userRoomRepository.findByUserAndRoom(user, room).orElseThrow();

        Message message = Message.builder()
                .userRoom(userRoom)
                .messageType(messageDto.getMessageType())
                .content(messageDto.getMessage())
                .build();

        messageRepository.save(message);
    }
}
