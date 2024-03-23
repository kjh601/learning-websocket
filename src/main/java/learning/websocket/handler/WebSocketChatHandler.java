package learning.websocket.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import learning.websocket.dto.MessageDto;
import learning.websocket.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketChatHandler extends TextWebSocketHandler {
    private final ObjectMapper mapper;

    private final Map<Long, Set<WebSocketSession>> chatRoomSessionMap = new ConcurrentHashMap<>();

    private final MessageService messageService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

        Long roomId = Long.valueOf((String) session.getAttributes().get("roomId"));

        if (!chatRoomSessionMap.containsKey(roomId)) {
            chatRoomSessionMap.put(roomId, ConcurrentHashMap.newKeySet());
        }

        Set<WebSocketSession> chatRoom = chatRoomSessionMap.get(roomId);

        chatRoom.add(session);
        log.info("{} 연결됨", session.getId());
    }

    // 소켓 통신 시 메세지의 전송을 다루는 부분
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        log.info("payload {}", payload);

        //페이로드 -> chatMessageDto 변환
        MessageDto messageDto = mapper.readValue(payload, MessageDto.class);
        log.info("session {}", messageDto.toString());

        Long roomId = Long.valueOf((String) session.getAttributes().get("roomId"));

        Set<WebSocketSession> chatRoomSession = chatRoomSessionMap.get(roomId);

        sendMessageToChatRoom(messageDto, chatRoomSession);

        messageService.save(messageDto, roomId);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {

        Long roomId = Long.valueOf((String) session.getAttributes().get("roomId"));
        Set<WebSocketSession> chatRoom = chatRoomSessionMap.get(roomId);

        chatRoom.remove(session);
        log.info("{} 연결 끊김", session.getId());
    }

    // chatRoom 에 있는 모든 session 에 메시지를 전송하라
    private void sendMessageToChatRoom(MessageDto messageDto, Set<WebSocketSession> chatRoomSession) {
        chatRoomSession.parallelStream().forEach(session -> sendMessage(session, messageDto));
    }

    // 메시지를 전송하라
    private void sendMessage(WebSocketSession session, MessageDto messageDto) {
        try {
            session.sendMessage(new TextMessage(mapper.writeValueAsString(messageDto)));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }
}
