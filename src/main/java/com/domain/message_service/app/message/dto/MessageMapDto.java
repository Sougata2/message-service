package com.domain.message_service.app.message.dto;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public record MessageMapDto(List<UUID> uuids, Map<UUID, MessageDto> messages) {
}
