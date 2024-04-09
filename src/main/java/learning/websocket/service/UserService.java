package learning.websocket.service;

import jakarta.transaction.Transactional;
import learning.websocket.converter.UserConverter;
import learning.websocket.dto.UserDto;
import learning.websocket.entity.User;
import learning.websocket.entity.UserRoom;
import learning.websocket.enums.UserRole;
import learning.websocket.repository.UserRepository;
import learning.websocket.repository.UserRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserConverter userConverter;

    private final BCryptPasswordEncoder encoder;
    private final UserRoomRepository userRoomRepository;

    public UserDto.userRes findUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        return userConverter.toUserRes(user);
    }

    public void signIn(String userId, String name, String password) {
        User user = User.builder()
                .loginId(userId)
                .name(name)
                .password(encoder.encode(password))
                .build();

        userRepository.save(user);
    }

    @Transactional
    public UserDto.userRes findAdmin(Long roomId) {
        UserRoom userRoom = userRoomRepository.findByRoomIdAndUserRole(roomId, UserRole.ADMIN).orElseThrow();
        User user = userRoom.getUser();
        return userConverter.toUserRes(user);
    }
}
