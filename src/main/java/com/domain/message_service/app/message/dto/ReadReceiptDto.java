package com.domain.message_service.app.message.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public final class ReadReceiptDto {
    private Long count;
    private UUID roomRef;
    private UUID lastSeen;
}
