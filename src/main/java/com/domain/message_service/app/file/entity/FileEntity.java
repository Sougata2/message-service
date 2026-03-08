package com.domain.message_service.app.file.entity;

import com.domain.message_service.app.file.enums.Status;
import com.domain.message_service.app.message.entity.MessageEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "files")
public class FileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String url;

    @Column
    private String originalName;

    @Column
    private Long size;

    @Column
    private String mimeType;

    @Enumerated(EnumType.STRING)
    private Status status;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    /**
     * RELATIONS
     */
    @ManyToOne
    @JoinColumn(name = "message_id")
    private MessageEntity message;
}
