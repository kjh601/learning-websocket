package learning.websocket.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

public class UserDto {

    @Getter
    @Builder
    @Jacksonized
    public static class joinReq{
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
    public static class userRes{
        private Long id;
        private String name;
    }
}
