package learning.websocket.converter;

import learning.websocket.dto.UserDto;
import learning.websocket.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {
    public User toUser(UserDto.joinReq req) {
        return User.builder()
                .name(req.getName())
                .build();
    }

    public UserDto.userRes toUserDto(User user) {
        return UserDto.userRes.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }
}
