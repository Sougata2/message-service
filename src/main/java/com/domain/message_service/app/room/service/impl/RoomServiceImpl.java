package com.domain.message_service.app.room.service.impl;

import com.domain.message_service.app.room.dto.RoomDto;
import com.domain.message_service.app.room.dto.RoomMapDto;
import com.domain.message_service.app.room.entity.RoomEntity;
import com.domain.message_service.app.room.enums.Type;
import com.domain.message_service.app.room.mapper.RoomMapper;
import com.domain.message_service.app.room.repository.RoomRepository;
import com.domain.message_service.app.room.service.RoomService;
import com.domain.message_service.app.user.dto.UserInfo;
import com.domain.message_service.client.auth.AuthClient;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {
    private final RoomRepository repository;
    private final AuthClient authClient;
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
    public RoomDto findPrivateRoomByParticipants(Long participant) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserInfo signedUser;
        try {
            signedUser = authClient.getUserInfo(username);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        RoomEntity room = repository.findPrivateRoomsByParticipants(List.of(participant, signedUser.id()))
                .orElseThrow(
                        () -> new EntityNotFoundException("Room is not found for participant %s"
                                .formatted(
                                        Stream.of(participant, signedUser.id())
                                                .map(String::valueOf)
                                                .collect(Collectors.joining(", "))
                                )
                        )
                );
        return mapper.toDto(room);
    }

    @Override
    public RoomMapDto getSubscribedRooms() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        UserInfo userInfo;
        try {
            userInfo = authClient.getUserInfo(username);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if (userInfo == null) {
            throw new RuntimeException("UserInfo is null");
        }
        List<UUID> uuids = repository.getSubscribedRoomsHash(userInfo.id());
        Map<UUID, RoomDto> roomMap = repository
                .getSubscribedRooms(userInfo.id())
                .stream()
                .collect(Collectors.toMap(RoomEntity::getReferenceNumber, mapper::toDto));

        return new RoomMapDto(uuids, roomMap);
    }

    @Override
    public List<UserInfo> getParticipants(UUID reference) {
        List<Long> participants = repository.findByReference(reference)
                .orElseThrow(
                        () -> new EntityNotFoundException("Room %s is not found".formatted(reference))
                ).getParticipants();

        List<UserInfo> participantsInfo;
        try {
            participantsInfo = participants
                    .stream()
                    .map(authClient::getUserById)
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return participantsInfo;
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
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserInfo signedUser;
        try {
            signedUser = authClient.getUserInfo(username);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // add the signed user.
        dto.getParticipants().add(signedUser.id());
        RoomEntity entity = mapper.toEntity(dto);
        entity.setType(Type.GROUP);
        RoomEntity saved = repository.save(entity);
        return mapper.toDto(saved);
    }

    @Override
    @Transactional
    public RoomDto createPrivate(RoomDto dto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserInfo signedUser;
        try {
            signedUser = authClient.getUserInfo(username);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // add the signed user.
        dto.getParticipants().add(signedUser.id());
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
