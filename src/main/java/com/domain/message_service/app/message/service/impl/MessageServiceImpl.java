package com.domain.message_service.app.message.service.impl;

import com.domain.message_service.app.message.dto.MessageDto;
import com.domain.message_service.app.message.entity.MessageEntity;
import com.domain.message_service.app.message.enums.Status;
import com.domain.message_service.app.message.mapper.MessageMapper;
import com.domain.message_service.app.message.repository.MessageRepository;
import com.domain.message_service.app.message.service.MessageService;
import com.domain.message_service.app.room.entity.RoomEntity;
import com.domain.message_service.app.room.repository.RoomRepository;
import com.domain.message_service.app.user.dto.UserInfo;
import com.domain.message_service.client.auth.AuthClient;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    private final RoomRepository roomRepository;
    private final MessageRepository repository;
    private final AuthClient authClient;
    private final MessageMapper mapper;


    @Override
    public List<MessageDto> findByRoom(UUID roomId) {
        RoomEntity room = roomRepository.findByReference(roomId)
                .orElseThrow(() -> new EntityNotFoundException("Room %s is not found".formatted(roomId)));
        List<MessageEntity> entities = repository.findByRoomId(room.getId());
        return entities.stream().map(mapper::toDto).toList();
    }

    @Override
    @Transactional
    public MessageDto save(MessageDto dto) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        UserInfo userInfo;
        try {
            userInfo = authClient.getUserInfo(email);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        RoomEntity room = roomRepository.findByReference(dto.getRoomRef())
                .orElseThrow(() -> new EntityNotFoundException("Room %s is not found".formatted(dto.getRoomRef())));
        MessageEntity entity = mapper.toEntity(dto);
        entity.setRoom(room);
        entity.setStatus(Status.SENT);

        // set sender details
        entity.setSenderId(userInfo.id());
        entity.setSenderEmail(userInfo.email());
        entity.setSenderFirstName(userInfo.firstName());
        entity.setSenderLastName(userInfo.lastName());

        MessageEntity saved = repository.save(entity);

        room.setLastMessage(saved);
        roomRepository.save(room);
        return mapper.toDto(saved);
    }

    @Override
    @Transactional
    public MessageDto update(MessageDto dto) {
        MessageEntity entity = repository.findByUuid(dto.getUuid())
                .orElseThrow(() -> new EntityNotFoundException("Message %s is not found".formatted(dto.getUuid())));

        if (!entity.getRoom().getReferenceNumber().equals(dto.getRoomRef())) {
            throw new IllegalArgumentException("Cannot update message with different room ID");
        }

        MessageEntity merged = mapper.partialUpdate(dto, entity);
        MessageEntity saved = repository.save(merged);
        return mapper.toDto(saved);
    }

    @Override
    @Transactional
    public MessageDto delete(MessageDto dto) {
        MessageEntity entity = repository.findByUuid(dto.getUuid())
                .orElseThrow(() -> new EntityNotFoundException("Message %s is not found".formatted(dto.getUuid())));
        repository.delete(entity);
        return dto;
    }
}
