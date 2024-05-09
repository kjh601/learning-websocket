package learning.websocket.dto;

import learning.websocket.enums.UserRole;
import lombok.Builder;
import lombok.Getter;

public class UserRoomDto {

    @Getter
    @Builder
    public static class joinReq {
        private Long userId;
        private Long roomId;
        private UserRole userRole;
    }

    @Getter
    @Builder
    public static class leaveReq {
        private Long userId;
        private Long roomId;
    }

    @Getter
    @Builder
    public static class userRes{
        private Long userId;
        private String userName;
        private Long roomId;
        private String roomName;
        private UserRole userRole;
    }
}
