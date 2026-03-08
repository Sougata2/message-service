package com.domain.message_service.app.file.mapper;

import com.domain.message_service.app.file.dto.FileDto;
import com.domain.message_service.app.file.entity.FileEntity;
import com.domain.message_service.app.message.mapper.MessageMapper;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {MessageMapper.class})
public interface FileMapper {
    FileEntity toEntity(FileDto fileDto);

    @Mapping(target = "messageUUID", source = "message.uuid")
    FileDto toDto(FileEntity fileEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    FileEntity partialUpdate(FileDto fileDto, @MappingTarget FileEntity fileEntity);
}