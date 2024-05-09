package learning.websocket.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RoomWithParticipantStatusDto {
    private Long id;
    private String name;
    private Integer currentMembers;
    private Integer maxMembers;
    private Boolean isParticipating;
}
