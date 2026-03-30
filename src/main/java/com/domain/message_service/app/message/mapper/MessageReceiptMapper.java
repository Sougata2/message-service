package com.domain.message_service.app.message.mapper;

import com.domain.message_service.app.message.dto.MessageReceiptDto;
import com.domain.message_service.app.message.entity.MessageEntity;
import com.domain.message_service.app.message.entity.MessageReceiptEntity;
import com.domain.message_service.app.participants.dto.ParticipantsDto;
import com.domain.message_service.app.participants.entity.ParticipantsEntity;
import com.domain.message_service.app.participants.mapper.ParticipantsMapper;
import com.domain.message_service.app.room.mapper.RoomMapper;
import org.mapstruct.*;

import java.util.UUID;

@Mapper(
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {RoomMapper.class, ParticipantsMapper.class, MessageMapper.class}
)
public interface MessageReceiptMapper {
    @Mappings({
            @Mapping(target = "participant", source = "participant", qualifiedByName = "mapParticipant"),
            @Mapping(target = "lastSeenMessage", source = "lastSeenMessage", qualifiedByName = "mapMessage"),
            @Mapping(target = "lastReceivedMessage", source = "lastReceivedMessage", qualifiedByName = "mapMessage")
    })
    MessageReceiptEntity toEntity(MessageReceiptDto messageReceiptDto);

    @Mapping(target = "participant", source = "participant.email")
    @Mapping(target = "lastSeenMessage", source = "lastSeenMessage.uuid")
    @Mapping(target = "lastReceivedMessage", source = "lastReceivedMessage.uuid")
    MessageReceiptDto toDto(MessageReceiptEntity messageReceiptEntity);

    @Mappings({
            @Mapping(target = "participant", source = "participant", qualifiedByName = "mapParticipant"),
            @Mapping(target = "lastSeenMessage", source = "lastSeenMessage", qualifiedByName = "mapMessage"),
            @Mapping(target = "lastReceivedMessage", source = "lastReceivedMessage", qualifiedByName = "mapMessage")
    })
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    MessageReceiptEntity partialUpdate(MessageReceiptDto messageReceiptDto, @MappingTarget MessageReceiptEntity messageReceiptEntity);

    @Named("mapParticipant")
    default ParticipantsEntity mapParticipant(String email) {
        if (email == null) return null;
        ParticipantsEntity participantsEntity = new ParticipantsEntity();
        participantsEntity.setEmail(email);
        return participantsEntity;
    }

    @Named("mapMessage")
    default MessageEntity mapMessage(UUID uuid) {
        if (uuid == null) return null;
        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setUuid(uuid);
        return messageEntity;
    }
}