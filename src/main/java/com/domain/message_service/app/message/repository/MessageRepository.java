package com.domain.message_service.app.message.repository;

import com.domain.message_service.app.message.entity.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MessageRepository extends JpaRepository<MessageEntity, Long> {
    Optional<MessageEntity> findByUuid(UUID uuid);

    @Query("select e from MessageEntity e where e.room.id = :roomId order by e.createdAt desc")
    List<MessageEntity> findByRoomId(Long roomId);

    @Query("select e.uuid from MessageEntity e where e.room.id = :roomId order by e.createdAt desc")
    List<UUID> getMessagesHash(Long roomId);
}