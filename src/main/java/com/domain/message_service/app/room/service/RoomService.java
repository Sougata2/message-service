package com.domain.message_service.app.room.service;

import com.domain.message_service.app.room.dto.RoomDto;
import com.domain.message_service.app.room.dto.RoomMapDto;
import com.domain.message_service.app.user.dto.UserInfo;

import java.util.List;
import java.util.UUID;

public interface RoomService {
    RoomDto findByReference(UUID uuid);

    RoomDto findPrivateRoomByParticipants(Long participants);

    RoomMapDto getSubscribedRooms();

    List<UserInfo> getParticipants(UUID reference);

    RoomDto create(RoomDto dto);

    RoomDto createGroup(RoomDto dto);

    RoomDto createPrivate(RoomDto dto);

    RoomDto update(RoomDto dto);

    RoomDto delete(RoomDto dto);
}
