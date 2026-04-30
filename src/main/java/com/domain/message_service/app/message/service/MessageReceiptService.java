package com.domain.message_service.app.message.service;

import com.domain.message_service.app.message.dto.MessageDto;
import com.domain.message_service.app.message.dto.ReadReceiptDto;
import com.domain.message_service.app.participants.entity.ParticipantsEntity;
import com.domain.message_service.app.room.entity.RoomEntity;

import java.util.List;
import java.util.Map;

public interface MessageReceiptService {
    Map<String, List<MessageDto>> acknowledge(List<MessageDto> acknowledgeableMessages);

    void createBulk(List<ParticipantsEntity> participants, RoomEntity room);

    List<ReadReceiptDto> getReadReceipts();
}
