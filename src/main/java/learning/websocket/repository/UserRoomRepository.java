package learning.websocket.repository;

import learning.websocket.entity.Room;
import learning.websocket.entity.User;
import learning.websocket.entity.UserRoom;
import learning.websocket.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRoomRepository extends JpaRepository<UserRoom, Long> {
    Optional<UserRoom> findByUserAndRoom(User user, Room room);
    List<UserRoom> findAllByRoom(Room room);
    Optional<UserRoom> findByRoomIdAndUserRole(Long roomId, UserRole userRole);
}
