package com.domain.message_service.app.room.entity;

import com.domain.message_service.app.message.entity.MessageEntity;
import com.domain.message_service.app.room.enums.Type;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "rooms")
public class RoomEntity {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, updatable = false)
    private UUID referenceNumber;

    @Enumerated(EnumType.STRING)
    private Type type;

    @Column(length = 100)
    private String name;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    /*
     * RELATIONS
     * */
    @OneToMany(mappedBy = "room", orphanRemoval = true)
    private List<MessageEntity> messages;

    @OneToOne
    @JoinColumn(name = "last_message_id")
    private MessageEntity lastMessage;

    @ElementCollection
    @CollectionTable(name = "room_participants", joinColumns = @JoinColumn(name = "room_id"))
    @Column(name = "user_id")
    private List<Long> participants;
}
