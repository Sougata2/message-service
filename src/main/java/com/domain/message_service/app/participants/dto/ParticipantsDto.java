package com.domain.message_service.app.participants.dto;

import com.domain.message_service.app.message.dto.MessageReceiptDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * DTO for {@link com.domain.message_service.app.participants.entity.ParticipantsEntity}
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ParticipantsDto implements Serializable {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private Long lastSeen;
    @JsonIgnore
    private List<MessageReceiptDto> messageReceipts;
}