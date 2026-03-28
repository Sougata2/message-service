package com.domain.message_service.app.message.mapper;

import com.domain.message_service.app.message.dto.MessageReceiptDto;
import com.domain.message_service.app.message.entity.MessageReceiptEntity;
import com.domain.message_service.app.participants.mapper.ParticipantsMapper;
import com.domain.message_service.app.room.mapper.RoomMapper;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {RoomMapper.class, ParticipantsMapper.class, MessageMapper.class, MessageMapper.class})
public interface MessageReceiptMapper {
    MessageReceiptEntity toEntity(MessageReceiptDto messageReceiptDto);

    @Mapping(target = "participant", source = "participant.email")
    @Mapping(target = "lastSeenMessage", source = "lastSeenMessage.uuid")
    @Mapping(target = "lastReceivedMessage", source = "lastReceivedMessage.uuid")
    MessageReceiptDto toDto(MessageReceiptEntity messageReceiptEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    MessageReceiptEntity partialUpdate(MessageReceiptDto messageReceiptDto, @MappingTarget MessageReceiptEntity messageReceiptEntity);
}