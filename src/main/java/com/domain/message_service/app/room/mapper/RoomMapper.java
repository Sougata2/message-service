package com.domain.message_service.app.room.mapper;

import com.domain.message_service.app.room.dto.RoomDto;
import com.domain.message_service.app.room.entity.RoomEntity;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface RoomMapper {
    RoomEntity toEntity(RoomDto roomDto);

    RoomDto toDto(RoomEntity roomEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    RoomEntity partialUpdate(RoomDto roomDto, @MappingTarget RoomEntity roomEntity);
}