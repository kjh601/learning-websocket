package learning.websocket.document;

import jakarta.persistence.*;
import learning.websocket.enums.MessageType;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Document(collection = "message")
@EntityListeners(AuditingEntityListener.class)
public class MessageDocument {

    @Id
    private String id;

    private MessageType messageType;

    private String content;

    @CreatedDate
    private LocalDateTime createAt;

    private Long userId;

    private String userName;

    private Long roomId;
}
