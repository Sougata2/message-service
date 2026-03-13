package com.domain.message_service.app.file.controller;

import com.domain.message_service.app.file.dto.FileDto;
import com.domain.message_service.app.file.properties.FileProperties;
import com.domain.message_service.app.file.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/files")
public class FileController {
    private final FileProperties properties;
    private final FileService service;

    @GetMapping("/room/{room}")
    public ResponseEntity<List<FileDto>> findByRoom(@PathVariable UUID room) {
        return ResponseEntity.ok(service.findByRoom(room));
    }

    @GetMapping("/message/{uuid}")
    public ResponseEntity<List<FileDto>> findByMessage(@PathVariable UUID uuid){
        return ResponseEntity.ok(service.findByMessage(uuid));
    }

    @GetMapping("/view/{filename:.+}")
    public ResponseEntity<Resource> view(
            @PathVariable String filename,
            @RequestParam(required = false) boolean download
    ) throws IOException {
        Path filePath = Paths.get(properties.getUploadDirectory()).resolve(filename);
        Resource resource = new UrlResource(filePath.toUri());

        if (!resource.exists()) throw new FileNotFoundException(filename);

        String contentType = Files.probeContentType(filePath);
        if (contentType == null) contentType = "application/octet-stream";

        String disposition = download ? "attachment" : "inline";

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, disposition + "; filename=\"" + filename + "\"")
                .body(resource);
    }

    @PostMapping("/upload")
    public ResponseEntity<List<FileDto>> upload(@RequestParam("files") List<MultipartFile> files) {
        return ResponseEntity.ok(service.upload(files));
    }
}
