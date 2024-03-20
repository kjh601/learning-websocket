package learning.websocket.controller;

import learning.websocket.converter.RoomConverter;
import learning.websocket.converter.UserRoomConverter;
import learning.websocket.dto.RoomDto;
import learning.websocket.dto.UserRoomDto;
import learning.websocket.entity.Room;
import learning.websocket.entity.UserRoom;
import learning.websocket.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rooms")
public class RoomController {

    private final RoomService roomService;
    private final RoomConverter roomConverter;
    private final UserRoomConverter userRoomConverter;

    @PostMapping("/")
    public RoomDto.roomRes create(@RequestBody RoomDto.createReq request) {
        Room room = roomService.createRoom(request);
        return roomConverter.toRoomDto(room);
    }

    @DeleteMapping("/")
    public void remove(@RequestBody RoomDto.removeReq request) {
        roomService.removeRoom(request);
    }

    @PostMapping("/join")
    public UserRoomDto.userRes joinRoom(@RequestBody UserRoomDto.joinReq request) {
        UserRoom userRoom = roomService.joinRoom(request);
        return userRoomConverter.toUserRoomDto(userRoom);
    }

    @DeleteMapping("/leave")
    public void leaveRoom(@RequestBody UserRoomDto.leaveReq request) {
        roomService.leaveRoom(request);
    }
}
