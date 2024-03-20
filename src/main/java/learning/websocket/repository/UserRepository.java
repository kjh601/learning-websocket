package learning.websocket.repository;

import learning.websocket.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

//    public Object deleteById(Long id);
}
