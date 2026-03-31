package com.domain.message_service.app.message.service.impl;

import com.domain.message_service.app.message.dto.AcknowledgementDto;
import com.domain.message_service.app.message.enums.Status;
import com.domain.message_service.app.message.repository.MessageReceiptRepository;
import com.domain.message_service.app.message.repository.MessageRepository;
import com.domain.message_service.app.message.service.MessageReceiptService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageReceiptServiceImpl implements MessageReceiptService {
    private final MessageReceiptRepository repository;
    private final MessageRepository messageRepository;

    @Async
    @Override
    @Transactional
    public void acknowledge(AcknowledgementDto dto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        for (Map.Entry<UUID, List<UUID>> element : dto.getMessageMap().entrySet()) {
            UUID roomId = element.getKey();
            List<UUID> messageIds = element.getValue();
            Status status = dto.getStatusMap().get(roomId);

            UUID lastMessageId = messageIds.getLast();

            // update the last received/seen pointer
            Long minId = null;
            if (status == Status.DELIVERED) {
                repository.updateLastReceived(lastMessageId, username, roomId);
                minId = repository.findMinimumLastReceived(roomId);
            } else if (status == Status.READ) {
                repository.updateLastSeen(lastMessageId, username, roomId);
                minId = repository.findMinLastSeen(roomId);
            }

            if (minId == null) return;

            // update the message status
            messageRepository.updateMessageStatus(minId, roomId, status);
        }
    }
}
