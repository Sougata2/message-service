package com.domain.message_service.app.message.mapper;

import com.domain.message_service.app.message.dto.MessageDto;
import com.domain.message_service.app.message.entity.MessageEntity;
import com.domain.message_service.app.participants.mapper.ParticipantsMapper;
import com.domain.message_service.app.room.mapper.RoomMapper;
import org.mapstruct.*;

@Mapper(
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {RoomMapper.class, ParticipantsMapper.class, MessageReceiptMapper.class}
)
public interface MessageMapper {
    @Mapping(target = "lastSeenMessageReceipts", ignore = true)
    @Mapping(target = "lastReceivedMessageReceipts", ignore = true)
    MessageEntity toEntity(MessageDto messageDto);

    @Mapping(target = "fileIds", ignore = true)
    @Mapping(target = "roomRef", source = "room.referenceNumber")
    @Mapping(target = "lastSeenMessageReceipts", ignore = true)
    @Mapping(target = "lastReceivedMessageReceipts", ignore = true)
    MessageDto toDto(MessageEntity messageEntity);

    @Mapping(target = "lastSeenMessageReceipts", ignore = true)
    @Mapping(target = "lastReceivedMessageReceipts", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    MessageEntity partialUpdate(MessageDto messageDto, @MappingTarget MessageEntity messageEntity);
}