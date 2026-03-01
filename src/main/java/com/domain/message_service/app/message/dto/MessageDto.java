package com.domain.message_service.app.message.dto;

import com.domain.message_service.app.message.enums.Media;
import com.domain.message_service.app.message.enums.Status;
import com.domain.message_service.app.room.dto.RoomDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for {@link com.domain.message_service.app.message.entity.MessageEntity}
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageDto implements Serializable {
    private Long id;
    private String message;
    private UUID uuid;
    private Status status;
    private Media media;
    private RoomDto room;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}