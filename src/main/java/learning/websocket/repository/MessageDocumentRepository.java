package learning.websocket.repository;

import learning.websocket.document.MessageDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MessageDocumentRepository extends MongoRepository<MessageDocument, String> {
    List<MessageDocument> findAllByRoomId(Long roomId);
}
