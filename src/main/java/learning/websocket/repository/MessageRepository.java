package learning.websocket.repository;

import learning.websocket.entity.Message;
import learning.websocket.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findAllByRoom(Room room);
}
