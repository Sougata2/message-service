package com.domain.message_service.app.file.service;

import com.domain.message_service.app.file.dto.FileDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface FileService {
    List<FileDto> findByRoom(UUID roomRef);

    List<FileDto> upload(List<MultipartFile> files);

    List<FileDto> findByMessage(UUID uuid);
}
