package com.domain.message_service.app.room.dto;

import com.domain.message_service.app.message.dto.MessageDto;
import com.domain.message_service.app.participants.dto.ParticipantsDto;
import com.domain.message_service.app.room.enums.Type;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * DTO for {@link com.domain.message_service.app.room.entity.RoomEntity}
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RoomDto implements Serializable {
    private Long id;
    private UUID referenceNumber;
    private Type type;
    private String name;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /*
     * RELATIONS
     * */
    @JsonIgnore
    private List<MessageDto> messages;
    private List<ParticipantsDto> participants;
    private MessageDto lastMessage;
}