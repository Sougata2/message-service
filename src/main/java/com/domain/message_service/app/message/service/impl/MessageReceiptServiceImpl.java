package com.domain.message_service.app.message.service.impl;

import com.domain.message_service.app.message.dto.MessageDto;
import com.domain.message_service.app.message.entity.MessageEntity;
import com.domain.message_service.app.message.entity.MessageReceiptEntity;
import com.domain.message_service.app.message.enums.Status;
import com.domain.message_service.app.message.repository.MessageReceiptRepository;
import com.domain.message_service.app.message.repository.MessageRepository;
import com.domain.message_service.app.message.service.MessageReceiptService;
import com.domain.message_service.app.participants.entity.ParticipantsEntity;
import com.domain.message_service.app.room.entity.RoomEntity;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class MessageReceiptServiceImpl implements MessageReceiptService {
    private final MessageReceiptRepository repository;
    private final MessageRepository messageRepository;

    @Override
    @Transactional
    public Map<String, List<MessageDto>> acknowledge(List<MessageDto> acknowledgeableMessages) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Map<String, List<MessageDto>> userMessageMap = new HashMap<>();
        acknowledgeableMessages.sort(Comparator.comparing(MessageDto::getCreatedAt));
        for (MessageDto element : acknowledgeableMessages) {

            Status acknowledgedStatus = element.getStatus();

            // update the last received/seen pointer
            MessageEntity min = null;
            if (acknowledgedStatus == Status.DELIVERED) {
                updateLastReceived(element.getId(), username, element.getRoomRef());
                min = repository.findMinimumLastReceived(element.getRoomRef(), element.getSenderEmail()).getFirst();
            } else if (acknowledgedStatus == Status.READ) {
                updateLastSeen(element.getId(), username, element.getRoomRef());
                min = repository.findMinLastSeen(element.getRoomRef(), element.getSenderEmail()).getFirst();
            }

            if (min == null) continue;

            // update the message status
            messageRepository.updateMessageStatus(min.getId(), element.getRoomRef(), min.getStatus());

            // set the final status.
            element.setStatus(min.getStatus());

            userMessageMap.computeIfAbsent(element.getSenderEmail(), k -> new ArrayList<>())
                    .add(element);
        }
        return userMessageMap;
    }

    @Override
    public void createBulk(List<ParticipantsEntity> participants, RoomEntity room) {
        List<MessageReceiptEntity> receipts = participants.stream()
                .map(
                        participantsEntity -> MessageReceiptEntity.builder()
                                .participant(participantsEntity)
                                .room(room)
                                .build()
                ).toList();
        repository.saveAll(receipts);
    }

    private void updateLastSeen(Long lastMessageId, String username, UUID roomId) {
        MessageEntity lastMessageEntity = messageRepository.findById(lastMessageId)
                .orElseThrow(() -> new EntityNotFoundException("Message %d is not found".formatted(lastMessageId)));
        MessageReceiptEntity messageReceiptEntity = repository.findByRoom_ReferenceNumberAndParticipant_Email(roomId, username)
                .orElseThrow(() -> new EntityNotFoundException("Message Receipt for room: %s and user: %s is not found".formatted(roomId, username)));
        messageReceiptEntity.setLastSeenMessage(lastMessageEntity);
        repository.save(messageReceiptEntity);
    }

    private void updateLastReceived(Long lastMessageId, String username, UUID roomId) {
        MessageEntity lastMessageEntity = messageRepository.findById(lastMessageId)
                .orElseThrow(() -> new EntityNotFoundException("Message %d is not found".formatted(lastMessageId)));
        MessageReceiptEntity messageReceiptEntity = repository.findByRoom_ReferenceNumberAndParticipant_Email(roomId, username)
                .orElseThrow(() -> new EntityNotFoundException("Message Receipt for room: %s and user: %s is not found".formatted(roomId, username)));
        messageReceiptEntity.setLastReceivedMessage(lastMessageEntity);
        repository.save(messageReceiptEntity);
    }
}
