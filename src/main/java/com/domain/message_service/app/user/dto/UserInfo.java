package com.domain.message_service.app.user.dto;

import com.domain.message_service.app.message.dto.MessageReceiptDto;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

public record UserInfo(
        Long id,
        String firstName,
        String lastName,
        String email,
        Long lastSeen,
        @JsonIgnore List<MessageReceiptDto> messageReceipts) {
}
