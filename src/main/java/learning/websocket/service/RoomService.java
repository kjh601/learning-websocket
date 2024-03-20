package learning.websocket.service;

import jakarta.transaction.Transactional;
import learning.websocket.converter.RoomConverter;
import learning.websocket.converter.UserRoomConverter;
import learning.websocket.dto.RoomDto;
import learning.websocket.dto.UserRoomDto;
import learning.websocket.entity.Room;
import learning.websocket.entity.User;
import learning.websocket.entity.UserRoom;
import learning.websocket.repository.RoomRepository;
import learning.websocket.repository.UserRepository;
import learning.websocket.repository.UserRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final UserRoomRepository userRoomRepository;
    private final UserRoomConverter userRoomConverter;

    private final UserRepository userRepository;

    private final RoomRepository roomRepository;
    private final RoomConverter roomConverter;

    public Room createRoom(RoomDto.createReq request) {
        Room room = roomConverter.toRoom(request);
        return roomRepository.save(room);
    }

    public void removeRoom(RoomDto.removeReq request) {
        roomRepository.deleteById(request.getId());
    }

    @Transactional
    public UserRoom joinRoom(UserRoomDto.joinReq request) {
        User user = userRepository.findById(request.getUserId()).get();
        Room room = roomRepository.findById(request.getRoomId()).get();

        UserRoom userRoom = userRoomConverter.toUserRoom(user, room, request.getUserRole());

        return userRoomRepository.save(userRoom);
    }

    @Transactional
    public void leaveRoom(UserRoomDto.leaveReq request) {
        User user = userRepository.findById(request.getUserId()).get();
        Room room = roomRepository.findById(request.getRoomId()).get();
        UserRoom userRoom = userRoomRepository.findByUserAndRoom(user, room);
        userRoomRepository.delete(userRoom);
    }
}