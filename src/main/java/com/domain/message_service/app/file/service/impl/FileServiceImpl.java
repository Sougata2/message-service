package com.domain.message_service.app.file.service.impl;

import com.domain.message_service.app.file.dto.FileDto;
import com.domain.message_service.app.file.entity.FileEntity;
import com.domain.message_service.app.file.enums.Status;
import com.domain.message_service.app.file.mapper.FileMapper;
import com.domain.message_service.app.file.properties.FileProperties;
import com.domain.message_service.app.file.repository.FileRepository;
import com.domain.message_service.app.file.service.FileService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
    private final FileProperties properties;
    private final FileRepository repository;
    private final FileMapper mapper;

    @Override
    public List<FileDto> findByRoom(UUID roomRef) {
        List<FileEntity> entities = repository.findByRoom(roomRef);
        return entities.stream().map(mapper::toDto).toList();
    }

    @Override
    @Transactional
    public List<FileDto> upload(List<MultipartFile> files) {
        List<FileEntity> entities = new ArrayList<>();
        for (MultipartFile file : files) {
            entities.add(store(file));
        }
        List<FileEntity> saved = repository.saveAll(entities);
        return saved.stream().map(mapper::toDto).toList();
    }

    @Override
    public List<FileDto> findByMessage(UUID uuid) {
        List<FileEntity> entities = repository.findByMessage(uuid);
        return entities.stream().map(mapper::toDto).toList();
    }

    private FileEntity store(MultipartFile file) {
        // 1. get upload directory create if not exist.
        Path root = Paths.get(properties.getUploadDirectory());
        try {
            if (Files.notExists(root)) {
                Files.createDirectories(root);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // 2. get the file extension
        String extension;
        if (file.getOriginalFilename() != null && !file.getOriginalFilename().isBlank()) {
            extension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
            // add extension validation if required later.
        } else {
            throw new RuntimeException("File name cannot be empty");
        }

        // 3. get the checksum
        String checksum;
        try (InputStream inputStream = file.getInputStream()) {
            checksum = DigestUtils.sha256Hex(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // 4. checksum + extension = target_file
        // 5. store the file (name = target_file).
        Path targetFile = root.resolve(Path.of(checksum + extension));
        if (Files.notExists(targetFile)) {
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, targetFile);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        // 6. return the entity.
        return FileEntity.builder()
                .url(targetFile.getFileName().toString())
                .originalName(file.getOriginalFilename())
                .size(file.getSize())
                .mimeType(file.getContentType())
                .status(Status.UPLOADED)
                .build();
    }
}
