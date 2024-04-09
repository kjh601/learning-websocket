package learning.websocket.repository;

import learning.websocket.dto.RoomWithParticipantStatusDto;
import learning.websocket.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {

    @Modifying
    @Query("UPDATE Room r SET r.currentMembers = r.currentMembers + 1 WHERE r.id = :id")
    void increaseCurrentMembers(@Param("id") Long id);

    @Modifying
    @Query("UPDATE Room r SET r.currentMembers = r.currentMembers - 1 WHERE r.id = :id")
    void decreaseCurrentMembers(@Param("id") Long id);

    @Query("SELECT new learning.websocket.dto.RoomWithParticipantStatusDto(r.id, r.roomName, r.currentMembers, r.maxMembers, CASE WHEN ur.user IS NOT NULL THEN true ELSE false END) " +
            "FROM Room r LEFT JOIN UserRoom ur ON r.id = ur.room.id AND ur.user.id = :userId")
    List<RoomWithParticipantStatusDto> findAllRoomsAndParticipationStatus(Long userId);
}