package com.domain.message_service.app.file.dto;

import com.domain.message_service.app.file.enums.Status;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for {@link com.domain.message_service.app.file.entity.FileEntity}
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class FileDto implements Serializable {
    private Long id;
    private String url;
    private String originalName;
    private Long size;
    private String mimeType;
    private Status status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UUID messageUUID;
}