package com.domain.message_service.app.message.repository;

import com.domain.message_service.app.message.entity.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MessageRepository extends JpaRepository<MessageEntity, Long> {
    Optional<MessageEntity> findByUuid(UUID uuid);

    List<MessageEntity> findByRoomId(Long roomId);
}