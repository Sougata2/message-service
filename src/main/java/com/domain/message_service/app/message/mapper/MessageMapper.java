package com.domain.message_service.app.message.mapper;

import com.domain.message_service.app.message.dto.MessageDto;
import com.domain.message_service.app.message.entity.MessageEntity;
import com.domain.message_service.app.room.mapper.RoomMapper;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {RoomMapper.class})
public interface MessageMapper {
    MessageEntity toEntity(MessageDto messageDto);

    @Mapping(target = "roomRef", source = "room.referenceNumber")
    MessageDto toDto(MessageEntity messageEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    MessageEntity partialUpdate(MessageDto messageDto, @MappingTarget MessageEntity messageEntity);
}