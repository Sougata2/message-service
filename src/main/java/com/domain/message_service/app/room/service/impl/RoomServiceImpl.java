package com.domain.message_service.app.room.service.impl;

import com.domain.message_service.app.participants.dto.ParticipantsDto;
import com.domain.message_service.app.participants.entity.ParticipantsEntity;
import com.domain.message_service.app.participants.mapper.ParticipantsMapper;
import com.domain.message_service.app.participants.repository.ParticipantsRepository;
import com.domain.message_service.app.room.dto.RoomDto;
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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {
    private final ParticipantsRepository participantsRepository;
    private final ParticipantsMapper participantsMapper;
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
    public List<RoomDto> getSubscribedRooms() {
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
        List<RoomEntity> entities = repository.getSubscribedRooms(userInfo.id());
        return entities.stream().map(mapper::toDto).toList();
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
        entity.setParticipants(collectMembers(dto.getParticipants()));
        RoomEntity saved = repository.save(entity);
        return mapper.toDto(saved);
    }

    @Override
    @Transactional
    public RoomDto createPrivate(RoomDto dto) {
        RoomEntity entity = mapper.toEntity(dto);
        entity.setParticipants(collectMembers(dto.getParticipants()));
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

    
    private List<ParticipantsEntity> collectMembers(List<ParticipantsDto> members) {
        // fetch the signed user
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserInfo signedUser;
        try {
            signedUser = authClient.getUserInfo(username);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // add the signed user to members list
        List<Long> participantsIds = new ArrayList<>(members.stream().map(ParticipantsDto::getId).toList());
        participantsIds.add(signedUser.id());

        // fetch the participants userInfos
        List<UserInfo> userInfos = authClient.getUsersByIds(participantsIds);

        // convert userInfos to ParticipantsDto
        List<ParticipantsEntity> participantsEntities = userInfos.stream().map(participantsMapper::userInfoToEntity).toList();

        return participantsRepository.saveAll(participantsEntities);
    }
}
