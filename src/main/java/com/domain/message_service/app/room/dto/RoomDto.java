package com.domain.message_service.app.room.dto;

import com.domain.message_service.app.message.entity.MessageEntity;
import com.domain.message_service.app.room.enums.Type;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * DTO for {@link com.domain.message_service.app.room.entity.RoomEntity}
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoomDto implements Serializable {
    private Long id;
    private UUID referenceNumber;
    private Type type;
    private String groupName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /*
     * RELATIONS
     * */
    @JsonIgnore
    private List<MessageEntity> messages;
    private List<Long> participants;
    private MessageEntity lastMessage;
}