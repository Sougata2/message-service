package com.domain.message_service.app.participants.mapper;

import com.domain.message_service.app.participants.dto.ParticipantsDto;
import com.domain.message_service.app.participants.entity.ParticipantsEntity;
import com.domain.message_service.app.user.dto.UserInfo;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ParticipantsMapper {
    ParticipantsEntity toEntity(ParticipantsDto participantsDto);

    ParticipantsDto toDto(ParticipantsEntity participantsEntity);

    ParticipantsEntity userInfoToEntity(UserInfo userInfo);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ParticipantsEntity partialUpdate(ParticipantsDto participantsDto, @MappingTarget ParticipantsEntity participantsEntity);
}