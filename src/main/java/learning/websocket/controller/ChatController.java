package learning.websocket.controller;

import learning.websocket.dto.MessageDto;
import learning.websocket.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatController {

    private final MessageService messageService;

    @MessageMapping("/rooms/{roomId}")
    @SendTo("/sub/rooms/{roomId}")
    public MessageDto chat(@DestinationVariable Long roomId, MessageDto message) {

        messageService.save(message, roomId);
        return message;
    }
}
