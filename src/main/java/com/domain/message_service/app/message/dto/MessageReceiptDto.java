package com.domain.message_service.app.message.dto;

import com.domain.message_service.app.room.dto.RoomDto;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

/**
 * DTO for {@link com.domain.message_service.app.message.entity.MessageReceiptEntity}
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MessageReceiptDto implements Serializable {
    private Long id;
    private RoomDto room;
    private String participant;
    private UUID lastReceivedMessage;
    private UUID lastSeenMessage;
}