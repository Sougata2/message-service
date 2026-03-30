package com.domain.message_service.app.participants.mapper;

import com.domain.message_service.app.message.mapper.MessageMapper;
import com.domain.message_service.app.message.mapper.MessageReceiptMapper;
import com.domain.message_service.app.participants.dto.ParticipantsDto;
import com.domain.message_service.app.participants.entity.ParticipantsEntity;
import com.domain.message_service.app.room.mapper.RoomMapper;
import com.domain.message_service.app.user.dto.UserInfo;
import org.mapstruct.*;

@Mapper(
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        uses = {RoomMapper.class, MessageMapper.class, MessageReceiptMapper.class}
)
public interface ParticipantsMapper {
    @Mapping(target = "messageReceipts", ignore = true)
    ParticipantsEntity toEntity(ParticipantsDto participantsDto);

    @Mapping(target = "messageReceipts", ignore = true)
    ParticipantsDto toDto(ParticipantsEntity participantsEntity);

    @Mapping(target = "messageReceipts", ignore = true)
    ParticipantsEntity userInfoToEntity(UserInfo userInfo);

    @Mapping(target = "messageReceipts", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ParticipantsEntity partialUpdate(ParticipantsDto participantsDto, @MappingTarget ParticipantsEntity participantsEntity);
}