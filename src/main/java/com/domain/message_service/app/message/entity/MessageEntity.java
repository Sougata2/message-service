package com.domain.message_service.app.message.entity;

import com.domain.message_service.app.message.enums.Media;
import com.domain.message_service.app.message.enums.Status;
import com.domain.message_service.app.message.enums.Type;
import com.domain.message_service.app.room.entity.RoomEntity;
import com.domain.message_service.app.user.dto.UserFields;
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

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "messages")
public class MessageEntity extends UserFields {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String message;

    @Column
    private UUID uuid;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Enumerated(EnumType.STRING)
    private Media media;

    @Enumerated(EnumType.STRING)
    private Type type;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    /**
     * RELATIONS
     */
    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private RoomEntity room;

    @OneToMany(mappedBy = "lastReceivedMessage", orphanRemoval = true)
    private List<MessageReceiptEntity> lastReceivedMessageReceipts;

    @OneToMany(mappedBy = "lastSeenMessage", orphanRemoval = true)
    private List<MessageReceiptEntity> lastSeenMessageReceipts;
}
