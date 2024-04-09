package learning.websocket.converter;

import learning.websocket.dto.UserRoomDto;
import learning.websocket.entity.Room;
import learning.websocket.entity.User;
import learning.websocket.entity.UserRoom;
import learning.websocket.enums.UserRole;
import org.springframework.stereotype.Component;

@Component
public class UserRoomConverter {
    public UserRoom toUserRoom(User user, Room room, UserRole userRole) {
        return UserRoom.builder()
                .user(user)
                .room(room)
                .userRole(userRole)
                .build();
    }


    public UserRoomDto.userRes toUserRoomDto(UserRoom userRoom) {
        return UserRoomDto.userRes.builder()
                .userId(userRoom.getUser().getId())
                .userName(userRoom.getUser().getName())
                .roomId(userRoom.getRoom().getId())
                .roomName(userRoom.getRoom().getRoomName())
                .userRole(userRoom.getUserRole())
                .build();
    }
}