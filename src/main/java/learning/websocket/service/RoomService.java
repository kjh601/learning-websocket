package learning.websocket.service;

import jakarta.transaction.Transactional;
import learning.websocket.converter.RoomConverter;
import learning.websocket.converter.UserRoomConverter;
import learning.websocket.dto.RoomDto;
import learning.websocket.dto.RoomWithParticipantStatusDto;
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

@Service
@RequiredArgsConstructor
public class RoomService {

    private final UserRoomRepository userRoomRepository;
    private final UserRoomConverter userRoomConverter;

    private final UserRepository userRepository;

    private final RoomRepository roomRepository;
    private final RoomConverter roomConverter;

    @Transactional
    public RoomDto.roomRes createRoom(RoomDto.createReq request, User user) {
        Room room = roomConverter.toCreateRoom(request);
        Room savedRoom = roomRepository.save(room);

        UserRoom userRoom = UserRoom.builder()
                .user(user)
                .userRole(UserRole.ADMIN)
                .room(savedRoom)
                .build();
        userRoomRepository.save(userRoom);
        roomRepository.increaseCurrentMembers(room.getId());

        return roomConverter.toRoomDto(savedRoom);
    }

    public void removeRoom(Long roomId) {
        roomRepository.deleteById(roomId);
    }

    @Transactional
    public void joinRoom(Long userId, Long roomId) {
        User user = userRepository.findById(userId).orElseThrow();
        Room room = roomRepository.findById(roomId).orElseThrow();
        UserRoom userRoom = userRoomConverter.toUserRoom(user, room, UserRole.MEMBER);

        roomRepository.increaseCurrentMembers(roomId);
        userRoomRepository.save(userRoom);
    }

    @Transactional
    public void leaveRoom(Long userId, Long roomId) {
        User user = userRepository.findById(userId).orElseThrow();
        Room room = roomRepository.findById(roomId).orElseThrow();
        UserRoom userRoom = userRoomRepository.findByUserAndRoom(user, room).orElseThrow();

        roomRepository.decreaseCurrentMembers(room.getId());
        userRoomRepository.delete(userRoom);
    }

    public List<RoomWithParticipantStatusDto> findAll(Long userId) {
        return roomRepository.findAllRoomsAndParticipationStatus(userId);
    }

    public RoomDto.roomRes findRoom(Long roomId) {

        Room room = roomRepository.findById(roomId).orElseThrow();
        return roomConverter.toRoomDto(room);
    }

    public RoomWithParticipantStatusDto findRoom(Long roomId, Long userId) {

        Room room = roomRepository.findById(roomId).orElseThrow();
        User user = userRepository.findById(userId).orElseThrow();
        Boolean isParticipating = userRoomRepository.findByUserAndRoom(user, room).isPresent();
        return roomConverter.toRoomDto(room, isParticipating);
    }

    public RoomDto.roomRes updateRoom(RoomDto.editReq request, Long roomId) {
        Room findRoom = roomRepository.findById(roomId).orElseThrow();
        Integer findRoomCurrentMembers = findRoom.getCurrentMembers();

        Room room = roomConverter.toEditRoom(request, roomId, findRoomCurrentMembers);

        Room savedRoom = roomRepository.save(room);
        return roomConverter.toRoomDto(savedRoom);
    }
}