package com.domain.message_service.app.message.service.impl;

import com.domain.message_service.app.message.dto.MessageDto;
import com.domain.message_service.app.message.dto.ReadReceiptDto;
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

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageReceiptServiceImpl implements MessageReceiptService {
    private final MessageReceiptRepository repository;
    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;

    @Override
    @Transactional
    public Map<String, List<MessageDto>> acknowledge(List<MessageDto> acknowledgeableMessages) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Map<UUID, MessageEntity> messageMap = messageRepository
                .findAllById(
                        acknowledgeableMessages.stream().map(MessageDto::getId).toList()
                )
                .stream()
                .collect(Collectors.toMap(MessageEntity::getUuid, e -> e));
        List<MessageEntity> acknowledgedMessages = new ArrayList<>();
        acknowledgeableMessages.sort(Comparator.comparing(MessageDto::getCreatedAt));
        for (MessageDto element : acknowledgeableMessages) {
            MessageEntity messageEntity = messageMap.get(element.getUuid());

            Status acknowledgedStatus = element.getStatus();

            // update the last received/seen pointer
            Long min = null;
            if (acknowledgedStatus == Status.DELIVERED) {
                updateLastReceived(element.getId(), username, element.getRoomRef());
                min = repository.findMinimumLastReceived(element.getRoomRef(), element.getSenderEmail());
            } else if (acknowledgedStatus == Status.READ) {
                updateLastSeen(element.getId(), username, element.getRoomRef());
                Long seenMin = repository.findMinLastSeen(element.getRoomRef(), element.getSenderEmail());
                if (Objects.equals(seenMin, element.getId())) {
                    min = seenMin;
                } else {
                    Long receivedMin = repository.findMinimumLastReceived(element.getRoomRef(), element.getSenderEmail());
                    if (Objects.equals(receivedMin, element.getId())) {
                        min = receivedMin;
                        acknowledgedStatus = Status.DELIVERED;
                    }
                }
            }

            if (min == null) continue;

            // update the message status
            if (Objects.equals(min, element.getId())) {
                messageEntity.setStatus(acknowledgedStatus);
                acknowledgedMessages.add(messageEntity);
            }

//            System.out.println("+++++++++++++++++++++++++++++++++++++++++++");
//            System.out.println("Message : " + messageEntity.getMessage());
//            System.out.println("Acknowledged By : " + username);
//            System.out.println("Acknowledged Status : " + acknowledgedStatus);
//            System.out.println("+++++++++++++++++++++++++++++++++++++++++++");
        }

        List<MessageEntity> saved = messageRepository.saveAll(acknowledgedMessages);
        return saved.stream().map(messageMapper::toDto).collect(Collectors.groupingBy(MessageDto::getSenderEmail));
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

    @Override
    public List<ReadReceiptDto> getReadReceipts() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return repository.getReadReceipts(username);
    }

    @Transactional
    public void updateLastSeen(Long lastMessageId, String username, UUID roomId) {
        MessageEntity lastMessageEntity = messageRepository.findById(lastMessageId)
                .orElseThrow(() -> new EntityNotFoundException("Message %d is not found".formatted(lastMessageId)));
        MessageReceiptEntity messageReceiptEntity = repository.findByRoom_ReferenceNumberAndParticipant_Email(roomId, username)
                .orElseThrow(() -> new EntityNotFoundException("Message Receipt for room: %s and user: %s is not found".formatted(roomId, username)));
        if (
                messageReceiptEntity.getLastSeenMessage() != null
                        && lastMessageId < messageReceiptEntity.getLastSeenMessage().getId()
        )
            return;
        messageReceiptEntity.setLastSeenMessage(lastMessageEntity);
        messageReceiptEntity.setLastReceivedMessage(lastMessageEntity);
        repository.save(messageReceiptEntity);
    }

    @Transactional
    public void updateLastReceived(Long lastMessageId, String username, UUID roomId) {
        MessageEntity lastMessageEntity = messageRepository.findById(lastMessageId)
                .orElseThrow(() -> new EntityNotFoundException("Message %d is not found".formatted(lastMessageId)));
        MessageReceiptEntity messageReceiptEntity = repository.findByRoom_ReferenceNumberAndParticipant_Email(roomId, username)
                .orElseThrow(() -> new EntityNotFoundException("Message Receipt for room: %s and user: %s is not found".formatted(roomId, username)));
        if (
                messageReceiptEntity.getLastReceivedMessage() != null
                        && lastMessageId < messageReceiptEntity.getLastReceivedMessage().getId()
        )
            return;
        messageReceiptEntity.setLastReceivedMessage(lastMessageEntity);
        repository.save(messageReceiptEntity);
    }
}
