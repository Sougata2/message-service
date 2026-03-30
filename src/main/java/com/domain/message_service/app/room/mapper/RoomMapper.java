package com.domain.message_service.app.room.mapper;

import com.domain.message_service.app.message.mapper.MessageMapper;
import com.domain.message_service.app.message.mapper.MessageReceiptMapper;
import com.domain.message_service.app.participants.mapper.ParticipantsMapper;
import com.domain.message_service.app.room.dto.RoomDto;
import com.domain.message_service.app.room.entity.RoomEntity;
import org.mapstruct.*;

@Mapper(
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {ParticipantsMapper.class, MessageMapper.class, MessageReceiptMapper.class}
)
public interface RoomMapper {
    @Mapping(target = "messageReceipts", ignore = true)
    RoomEntity toEntity(RoomDto roomDto);

    @Mapping(target = "messages", ignore = true)
    @Mapping(target = "messageReceipts", ignore = true)
    RoomDto toDto(RoomEntity roomEntity);

    @Mapping(target = "messageReceipts", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    RoomEntity partialUpdate(RoomDto roomDto, @MappingTarget RoomEntity roomEntity);
}