package com.domain.message_service.app.message.dto;

import com.domain.message_service.app.message.enums.Media;
import com.domain.message_service.app.message.enums.Status;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for {@link com.domain.message_service.app.message.entity.MessageEntity}
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MessageDto implements Serializable {
    private Long id;
    private String message;
    private UUID uuid;
    private Status status;
    private Media media;
    private UUID roomRef;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long senderId;
    private String senderEmail;
    private String senderFirstName;
    private String senderLastName;
}