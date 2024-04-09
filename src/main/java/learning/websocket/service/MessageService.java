package learning.websocket.service;

import jakarta.transaction.Transactional;
import learning.websocket.converter.MessageConverter;
import learning.websocket.dto.MessageDto;
import learning.websocket.entity.Message;
import learning.websocket.entity.Room;
import learning.websocket.entity.User;
import learning.websocket.repository.MessageRepository;
import learning.websocket.repository.RoomRepository;
import learning.websocket.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final MessageConverter messageConverter;

    private final UserRepository userRepository;
    private final RoomRepository roomRepository;

    public void save(MessageDto.saveReq saveReq, Long roomId) {

        User user = userRepository.findById(saveReq.getSenderId()).orElseThrow();
        Room room = roomRepository.findById(roomId).orElseThrow();

        Message message = messageConverter.toMessage(saveReq, user, room);

        messageRepository.save(message);
    }

    @Transactional
    public List<MessageDto.logRes> getMessages(Long roomId, Long userId) {
        Room findRoom = roomRepository.findById(roomId).orElseThrow();

        return messageRepository.findAllByRoom(findRoom).stream()
                .map(room -> messageConverter.toLogRes(room, userId))
                .collect(Collectors.toList());
    }
}
