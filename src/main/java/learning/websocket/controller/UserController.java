package learning.websocket.controller;

import learning.websocket.converter.UserConverter;
import learning.websocket.dto.UserDto;
import learning.websocket.entity.User;
import learning.websocket.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final UserConverter userConverter;

    @PostMapping("/")
    public UserDto.userRes join(@RequestBody UserDto.joinReq request) {
        User user = userService.joinUser(request);
        return userConverter.toUserDto(user);
    }

    @DeleteMapping("/")
    public void remove(@RequestBody UserDto.removeReq request) {
        userService.removeUser(request);
    }
}
