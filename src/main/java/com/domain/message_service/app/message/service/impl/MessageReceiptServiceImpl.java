package com.domain.message_service.app.message.service.impl;

import com.domain.message_service.app.message.dto.AcknowledgeableMessage;
import com.domain.message_service.app.message.dto.AcknowledgementDto;
import com.domain.message_service.app.message.dto.MessageDto;
import com.domain.message_service.app.message.entity.MessageEntity;
import com.domain.message_service.app.message.entity.MessageReceiptEntity;
import com.domain.message_service.app.message.enums.Status;
import com.domain.message_service.app.message.mapper.MessageMapper;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageReceiptServiceImpl implements MessageReceiptService {
    private final MessageReceiptRepository repository;
    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;

    @Override
    @Transactional
    public Map<String, List<MessageDto>> acknowledge(AcknowledgementDto dto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Map<String, List<MessageDto>> userMessageMap = new HashMap<>();
        for (Map.Entry<UUID, List<AcknowledgeableMessage>> element : dto.getRoomMessageMap().entrySet()) {
            UUID roomId = element.getKey();
            List<AcknowledgeableMessage> messageIds = element.getValue();
            Status status = dto.getStatusMap().get(roomId);

            AcknowledgeableMessage lastMessage = messageIds.getLast();

            // update the last received/seen pointer
            Long minId = null;
            if (status == Status.DELIVERED) {
                updateLastReceived(lastMessage.getId(), username, roomId);
                minId = repository.findMinimumLastReceived(roomId, lastMessage.getSenderEmail());
            } else if (status == Status.READ) {
                updateLastSeen(lastMessage.getId(), username, roomId);
                minId = repository.findMinLastSeen(roomId, lastMessage.getSenderEmail());
            }

            if (minId == null) return null;


            // update the message status
            messageRepository.updateMessageStatus(minId, roomId, status);

            Long fromId = messageIds.getFirst().getId();
            Long toId = minId;
            if (toId >= fromId) {
                List<MessageEntity> messages = messageRepository.getMessageInRange(roomId, username, fromId, toId);
                Map<String, List<MessageEntity>> grouped = messages.stream()
                        .collect(Collectors.groupingBy(MessageEntity::getSenderEmail));
                grouped.forEach((senderEmail, list) ->
                        userMessageMap.merge(senderEmail, list.stream().map(messageMapper::toDto).toList(), (existing, incoming) -> {
                            existing.addAll(incoming);
                            return existing;
                        })
                );
            }
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
