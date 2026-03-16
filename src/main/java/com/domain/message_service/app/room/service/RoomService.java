package com.domain.message_service.app.room.service;

import com.domain.message_service.app.participants.dto.ParticipantsDto;
import com.domain.message_service.app.room.dto.RoomDto;

import java.util.List;
import java.util.UUID;

public interface RoomService {
    RoomDto findByReference(UUID uuid);

    RoomDto findPrivateRoomByParticipants(Long participants);

    List<RoomDto> getSubscribedRooms();

    List<ParticipantsDto> findChatPartners();

    RoomDto create(RoomDto dto);

    RoomDto createGroup(RoomDto dto);

    RoomDto createPrivate(RoomDto dto);

    RoomDto update(RoomDto dto);

    RoomDto delete(RoomDto dto);
}
