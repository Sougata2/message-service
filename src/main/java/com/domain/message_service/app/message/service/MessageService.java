package com.domain.message_service.app.message.service;

import com.domain.message_service.app.message.dto.MessageDto;
import com.domain.message_service.app.message.dto.MessageMapDto;

import java.util.UUID;

public interface MessageService {
    MessageMapDto findByRoom(UUID roomId);

    MessageDto save(MessageDto dto);

    MessageDto update(MessageDto dto);

    MessageDto delete(MessageDto dto);
}
