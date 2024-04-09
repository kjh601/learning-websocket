package learning.websocket.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

public class RoomDto {

    @Getter
    @Builder
    @ToString
    public static class createReq {
        private String name;
        private Integer maxMembers;
    }

    @Getter
    @Builder
    @ToString
    public static class editReq {
        private String name;
        private Integer maxMembers;
    }

    @Getter
    @Builder
    @ToString
    public static class roomRes{
        private Long id;
        private String name;
        private Integer currentMembers;
        private Integer maxMembers;
    }
}
