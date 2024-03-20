package learning.websocket.entity;

import jakarta.persistence.*;
import learning.websocket.enums.MessageType;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MessageType messageType;

    @Column(nullable = false)
    private String content;

    @CreatedDate
    private LocalDateTime createAt;

    @ManyToOne(fetch = FetchType.LAZY)
    private UserRoom userRoom;
}
