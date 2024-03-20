package learning.websocket.converter;

import learning.websocket.dto.RoomDto;
import learning.websocket.entity.Room;
import org.springframework.stereotype.Component;

@Component
public class RoomConverter {
    public Room toRoom(RoomDto.createReq req) {
        return Room.builder()
                .roomName(req.getName())
                .build();
    }

    public RoomDto.roomRes toRoomDto(Room room) {
        return RoomDto.roomRes.builder()
                .id(room.getId())
                .name(room.getRoomName())
                .build();
    }
}
