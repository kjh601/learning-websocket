package learning.websocket.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import learning.websocket.dto.ChatMessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketChatHandler extends TextWebSocketHandler {
    private final ObjectMapper mapper;

    //현재 연결된 세션들
    private final Set<WebSocketSession> sessions = new HashSet<>();

    // chatRoomId: {session1, session2}
    private final Map<Long, Set<WebSocketSession>> chatRoomSessionMap = new HashMap<>();

    // 소켓 연결 확인
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
        ChatMessageDto chatMessageDto = mapper.readValue(payload, ChatMessageDto.class);
        log.info("session {}", chatMessageDto.toString());

        Long chatRoomId = chatMessageDto.getChatRoomId();

        // 메모리 상에 채팅방에 대한 세션 없으면 만들어줌
        // 궁금한점 : 문자를 보냈는데 해당 방이 없다고 그 방을 만들어준다고? 해당 방이 없는데 어케 메시지를 보낸겨, 방이 있고 문자가 있는거지
        // TODO 없는 방의 ID로 요청 보낸 경우 예외 처리해야 함
        if (!chatRoomSessionMap.containsKey(chatRoomId)) {
            chatRoomSessionMap.put(chatRoomId, new HashSet<>());
        }
        // 채팅방 Id로 채팅방 찾아서 chatRoomSession 에 할당
        Set<WebSocketSession> chatRoomSession = chatRoomSessionMap.get(chatRoomId);

        // 메시지가 ENTER 타입인 경우, chatRoomSession 채팅방에 추가하라
        // TODO 이미 chatRoomSession 에 있는데 ENTER 인 경우 예외 처리할 것
        if (chatMessageDto.getMessageType().equals(ChatMessageDto.MessageType.ENTER)) {
            chatRoomSession.add(session);
        }

        // chatRoomSession 채팅방 사이즈가 3 이상이면, removeClosedSession 를 실행하라
        if (chatRoomSession.size() >= 3) {
            removeClosedSession(chatRoomSession);
        }
        sendMessageToChatRoom(chatMessageDto, chatRoomSession);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {

        //TODO Auto-generated method stub
        log.info("{} 연결 끊김", session.getId());
        sessions.remove(session);
    }

    // 채팅방에 있는 세션 중에서 현재 연결된 세션 집합에 없는 세션은 삭제하라
    private void removeClosedSession(Set<WebSocketSession> chatRoomSession) {
        chatRoomSession.removeIf(session -> !sessions.contains(session));
    }

    private void sendMessageToChatRoom(ChatMessageDto chatMessageDto, Set<WebSocketSession> chatRoomSession) {
        chatRoomSession.parallelStream().forEach(session -> sendMessage(session, chatMessageDto));
    }

    private <T> void sendMessage(WebSocketSession session, T chatMessageDto) {
        try {
            session.sendMessage(new TextMessage(mapper.writeValueAsString(chatMessageDto)));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }
}
