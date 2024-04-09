package learning.websocket.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 40)
    private String roomName;

    @Column(nullable = false)
    @ColumnDefault("0")
    private Integer currentMembers;

    @Column(nullable = false)
    private Integer maxMembers;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    private List<UserRoom> userRooms = new ArrayList<>();

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    private List<Message> messages = new ArrayList<>();
}
