package learning.websocket.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import learning.websocket.dto.MessageDto;
import learning.websocket.enums.MessageType;
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

    private final Set<WebSocketSession> sessions = ConcurrentHashMap.newKeySet();

    private final Map<Long, Set<WebSocketSession>> chatRoomSessionMap = new ConcurrentHashMap<>();

    private final MessageService messageService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // TODO Auto-generated method stub
        log.info("{} 연결됨", session.getId());
        sessions.add(session);
    }

    // 소켓 통신 시 메세지의 전송을 다루는 부분
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        log.info("payload {}", payload);

        //페이로드 -> chatMessageDto 변환
        MessageDto messageDto = mapper.readValue(payload, MessageDto.class);
        log.info("session {}", messageDto.toString());

        Long chatRoomId = messageDto.getChatRoomId();

        // 메모리 상에 채팅방에 대한 세션 없으면 만들어줌
        if (!chatRoomSessionMap.containsKey(chatRoomId)) {
            chatRoomSessionMap.put(chatRoomId, ConcurrentHashMap.newKeySet());
        }

        // 채팅방 Id로 채팅방 찾아서 chatRoomSession 에 할당
        Set<WebSocketSession> chatRoomSession = chatRoomSessionMap.get(chatRoomId);

        // 메시지가 ENTER 타입인 경우, chatRoomSession 채팅방에 추가하라
        if (messageDto.getMessageType().equals(MessageType.ENTER)) {
            chatRoomSession.add(session);
        }

        removeClosedSession(chatRoomSession);

        sendMessageToChatRoom(messageDto, chatRoomSession);

        messageService.save(messageDto);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("{} 연결 끊김", session.getId());
        sessions.remove(session);
    }

    // 채팅방에 있는 세션 중에서 현재 연결된 세션 집합에 없는 세션은 삭제하라
    private void removeClosedSession(Set<WebSocketSession> chatRoomSession) {
        chatRoomSession.removeIf(session -> !sessions.contains(session));
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
