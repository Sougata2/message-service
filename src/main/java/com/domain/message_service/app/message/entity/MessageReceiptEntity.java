package com.domain.message_service.app.message.entity;

import com.domain.message_service.app.participants.entity.ParticipantsEntity;
import com.domain.message_service.app.room.entity.RoomEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "message_receipts")
public class MessageReceiptEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private RoomEntity room;

    @ManyToOne
    @JoinColumn(name = "participant_id")
    private ParticipantsEntity participant;

    @ManyToOne
    @JoinColumn(name = "last_received_message_id")
    private MessageEntity lastReceivedMessage;

    @ManyToOne
    @JoinColumn(name = "last_seen_message_id")
    private MessageEntity lastSeenMessage;
}
