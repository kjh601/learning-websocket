package learning.websocket.service;

import jakarta.transaction.Transactional;
import learning.websocket.converter.UserRoomConverter;
import learning.websocket.dto.UserRoomDto;
import learning.websocket.entity.Room;
import learning.websocket.entity.User;
import learning.websocket.entity.UserRoom;
import learning.websocket.enums.UserRole;
import learning.websocket.repository.RoomRepository;
import learning.websocket.repository.UserRepository;
import learning.websocket.repository.UserRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserRoomService {
    private final UserRoomRepository userRoomRepository;
    private final UserRoomConverter userRoomConverter;

    private final UserRepository userRepository;
    private final RoomRepository roomRepository;

    public Boolean checkAdmin(Long userId, Long roomId) {
        User user = userRepository.findById(userId).orElseThrow();
        Room room = roomRepository.findById(roomId).orElseThrow();
        Optional<UserRoom> userRoomOptional = userRoomRepository.findByUserAndRoom(user, room);

        return userRoomOptional.map(userRoom -> userRoom.getUserRole() == UserRole.ADMIN).orElse(false);
    }

    @Transactional
    public List<UserRoomDto.userRes> findAllParticipants(Long roomId) {
        Room room = roomRepository.findById(roomId).orElseThrow();
        List<UserRoom> userRooms = userRoomRepository.findAllByRoom(room);

        return userRooms.stream()
                .map(userRoomConverter::toUserRoomDto)
                .collect(Collectors.toList());
    }

    public boolean checkEnter(Long userId, Long roomId) {
        User user = userRepository.findById(userId).orElseThrow();
        Room room = roomRepository.findById(roomId).orElseThrow();
        return userRoomRepository.findByUserAndRoom(user, room).isPresent();
    }
}
