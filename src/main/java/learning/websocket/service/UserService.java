package learning.websocket.service;

import learning.websocket.converter.UserConverter;
import learning.websocket.dto.UserDto;
import learning.websocket.entity.User;
import learning.websocket.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserConverter userConverter;

    public User joinUser(UserDto.joinReq req){

        User user = userConverter.toUser(req);

        return userRepository.save(user);
    }

    public void removeUser(UserDto.removeReq req){
        userRepository.deleteById(req.getId());
    }
}
