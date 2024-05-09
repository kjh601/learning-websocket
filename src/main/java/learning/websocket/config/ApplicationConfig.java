package learning.websocket.config;

import learning.websocket.converter.MessageConverter;
import learning.websocket.repository.MessageDocumentRepository;
import learning.websocket.repository.MessageRepository;
import learning.websocket.repository.RoomRepository;
import learning.websocket.repository.UserRepository;
import learning.websocket.service.MessageService;
import learning.websocket.service.MongoDBMessageServiceImpl;
import learning.websocket.service.MySQLMessageServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {
    private final MessageRepository messageRepository;
    private final MessageDocumentRepository messageDocumentRepository;

    private final MessageConverter messageConverter;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final SimpMessagingTemplate template;


    @Bean
    public MessageService messageService() {

//        return new MySQLMessageServiceImpl(
//        messageRepository,
//        messageConverter,
//        userRepository,
//        roomRepository,
//        template);

        return new MongoDBMessageServiceImpl(
                messageDocumentRepository,
                messageConverter,
                userRepository,
                roomRepository,
                template);
    }
}
