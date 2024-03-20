package learning.websocket.repository;

import learning.websocket.dto.MessageLogDto;
import learning.websocket.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("SELECT new learning.websocket.dto.MessageLogDto(m.messageType, ur.room.id, ur.user.id, m.createAt, m.content) " +
            "FROM Message m " +
            "JOIN UserRoom ur ON m.userRoom.id = ur.id " +
            "WHERE ur.room.id = :roomId " +
            "ORDER BY m.createAt"
    )
    List<MessageLogDto> findMessageLogs(Long roomId);
}
