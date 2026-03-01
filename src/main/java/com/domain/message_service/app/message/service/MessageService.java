package com.domain.message_service.app.message.service;

import com.domain.message_service.app.message.dto.MessageDto;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    List<MessageDto> findByRoom(UUID roomId);

    MessageDto save(MessageDto dto);

    MessageDto update(MessageDto dto);

    MessageDto delete(MessageDto dto);
}
