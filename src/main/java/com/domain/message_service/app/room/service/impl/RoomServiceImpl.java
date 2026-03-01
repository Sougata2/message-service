package com.domain.message_service.app.room.service.impl;

import com.domain.message_service.app.room.dto.RoomDto;
import com.domain.message_service.app.room.entity.RoomEntity;
import com.domain.message_service.app.room.enums.Type;
import com.domain.message_service.app.room.mapper.RoomMapper;
import com.domain.message_service.app.room.repository.RoomRepository;
import com.domain.message_service.app.room.service.RoomService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {
    private final RoomRepository repository;
    private final RoomMapper mapper;

    @Override
    public RoomDto findByReference(UUID uuid) {
        RoomEntity entity = repository.findByReference(uuid)
                .orElseThrow(
                        () -> new EntityNotFoundException("Room %s is not found".formatted(uuid))
                );
        return mapper.toDto(entity);
    }

    @Override
    @Transactional
    public RoomDto create(RoomDto dto) {
        RoomEntity entity = mapper.toEntity(dto);
        RoomEntity saved = repository.save(entity);
        return mapper.toDto(saved);
    }

    @Override
    @Transactional
    public RoomDto createGroup(RoomDto dto) {
        RoomEntity entity = mapper.toEntity(dto);
        entity.setType(Type.GROUP);
        RoomEntity saved = repository.save(entity);
        return mapper.toDto(saved);
    }

    @Override
    @Transactional
    public RoomDto createPrivate(RoomDto dto) {
        RoomEntity entity = mapper.toEntity(dto);
        entity.setType(Type.PRIVATE);
        RoomEntity saved = repository.save(entity);
        return mapper.toDto(saved);
    }

    @Override
    @Transactional
    public RoomDto update(RoomDto dto) {
        RoomEntity entity = repository.findByReference(dto.getReferenceNumber())
                .orElseThrow(() -> new EntityNotFoundException("Room %s is not found".formatted(dto.getReferenceNumber())));
        RoomEntity merged = mapper.partialUpdate(dto, entity);
        RoomEntity saved = repository.save(merged);
        return mapper.toDto(saved);
    }

    @Override
    @Transactional
    public RoomDto delete(RoomDto dto) {
        RoomEntity entity = repository.findByReference(dto.getReferenceNumber())
                .orElseThrow(() -> new EntityNotFoundException("Room %s is not found".formatted(dto.getReferenceNumber())));
        repository.delete(entity);
        return dto;
    }
}
