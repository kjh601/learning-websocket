package learning.websocket.converter;

import learning.websocket.dto.RoomDto;
import learning.websocket.dto.RoomWithParticipantStatusDto;
import learning.websocket.entity.Room;
import org.springframework.stereotype.Component;

@Component
public class RoomConverter {
    public Room toCreateRoom(RoomDto.createReq req) {
        return Room.builder()
                .currentMembers(0)
                .roomName(req.getName())
                .maxMembers(req.getMaxMembers())
                .build();
    }

    public Room toEditRoom(RoomDto.editReq req, Long roomId, Integer currentMembers) {
        return Room.builder()
                .id(roomId)
                .currentMembers(currentMembers)
                .roomName(req.getName())
                .maxMembers(req.getMaxMembers())
                .build();
    }

    public RoomDto.roomRes toRoomDto(Room room) {
        return RoomDto.roomRes.builder()
                .id(room.getId())
                .name(room.getRoomName())
                .currentMembers(room.getCurrentMembers())
                .maxMembers(room.getMaxMembers())
                .build();
    }

    public RoomWithParticipantStatusDto toRoomDto(Room room, Boolean isParticipating) {
        return RoomWithParticipantStatusDto.builder()
                .id(room.getId())
                .name(room.getRoomName())
                .currentMembers(room.getCurrentMembers())
                .maxMembers(room.getMaxMembers())
                .isParticipating(isParticipating)
                .build();
    }
}
