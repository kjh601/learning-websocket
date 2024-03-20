package learning.websocket.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

public class RoomDto {

    @Getter
    @Builder
    @Jacksonized
    public static class createReq {
        private String name;
    }

    @Getter
    @Builder
    @Jacksonized
    public static class removeReq{
        private Long id;
    }

    @Getter
    @Builder
    public static class roomRes{
        private Long id;
        private String name;
    }
}
