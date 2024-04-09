package learning.websocket.config;

import jakarta.transaction.Transactional;
import learning.websocket.entity.User;
import learning.websocket.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        Optional<User> findResult = userRepository.findByLoginId(userId);
        if (findResult.isEmpty()) {
            throw new UsernameNotFoundException("해당 사용자를 찾을 수 없습니다.");
        }
        User user = findResult.get();
        return new PrincipalDetails(user);
    }
}
