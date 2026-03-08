package com.domain.message_service.app.message.service.impl;

import com.domain.message_service.app.file.entity.FileEntity;
import com.domain.message_service.app.file.repository.FileRepository;
import com.domain.message_service.app.message.dto.MessageDto;
import com.domain.message_service.app.message.entity.MessageEntity;
import com.domain.message_service.app.message.enums.Media;
import com.domain.message_service.app.message.enums.Status;
import com.domain.message_service.app.message.mapper.MessageMapper;
import com.domain.message_service.app.message.repository.MessageRepository;
import com.domain.message_service.app.message.service.MessageService;
import com.domain.message_service.app.participants.entity.ParticipantsEntity;
import com.domain.message_service.app.participants.repository.ParticipantsRepository;
import com.domain.message_service.app.room.entity.RoomEntity;
import com.domain.message_service.app.room.repository.RoomRepository;
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
    private final ParticipantsRepository participantsRepository;
    private final FileRepository fileRepository;
    private final RoomRepository roomRepository;
    private final MessageRepository repository;
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
        ParticipantsEntity signedUser = participantsRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User %s is not found".formatted(email)));

        RoomEntity room = roomRepository.findByReference(dto.getRoomRef())
                .orElseThrow(() -> new EntityNotFoundException("Room %s is not found".formatted(dto.getRoomRef())));
        MessageEntity entity = mapper.toEntity(dto);
        entity.setRoom(room);
        entity.setStatus(Status.SENT);

        // set sender details
        entity.setSenderId(signedUser.getId());
        entity.setSenderEmail(signedUser.getEmail());
        entity.setSenderFirstName(signedUser.getFirstName());
        entity.setSenderLastName(signedUser.getLastName());

        MessageEntity saved = repository.save(entity);

        // attach the files
        if (saved.getMedia() != Media.TEXT && dto.getFileIds() != null && !dto.getFileIds().isEmpty()) {
            attachFiles(dto.getFileIds(), saved);
        }

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


    private void attachFiles(List<Long> ids, MessageEntity messageEntity) {
        List<FileEntity> files = fileRepository.findAllById(ids);
        files.forEach(fileEntity -> fileEntity.setMessage(messageEntity));
        fileRepository.saveAll(files);
    }
}
