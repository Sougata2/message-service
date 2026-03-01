package com.domain.message_service.app.room.service;

import com.domain.message_service.app.room.dto.RoomDto;

import java.util.UUID;

public interface RoomService {
    RoomDto findByReference(UUID uuid);

    RoomDto create(RoomDto dto);
    
    RoomDto createGroup(RoomDto dto);

    RoomDto createPrivate(RoomDto dto);

    RoomDto update(RoomDto dto);

    RoomDto delete(RoomDto dto);
}
