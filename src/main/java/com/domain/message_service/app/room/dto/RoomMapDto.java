package com.domain.message_service.app.room.dto;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public record RoomMapDto(List<UUID> uuids, Map<UUID, RoomDto> rooms) {
}
