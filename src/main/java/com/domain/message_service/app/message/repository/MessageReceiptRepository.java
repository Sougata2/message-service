package com.domain.message_service.app.message.repository;

import com.domain.message_service.app.message.entity.MessageEntity;
import com.domain.message_service.app.message.entity.MessageReceiptEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MessageReceiptRepository extends JpaRepository<MessageReceiptEntity, Long> {
    Optional<MessageReceiptEntity> findByRoom_ReferenceNumberAndParticipant_Email(UUID roomReferenceNumber, String participantEmail);

    @Query("""
            select e.lastReceivedMessage
            from MessageReceiptEntity e
            where e.room.referenceNumber = :roomId
            and e.participant.email <> :sender
            order by e.lastReceivedMessage.createdAt
            """)
    List<MessageEntity> findMinimumLastReceived(UUID roomId, String sender);

    @Query("""
            select e.lastSeenMessage
            from MessageReceiptEntity e
            where e.room.referenceNumber = :roomId
            and e.participant.email <> :sender
            order by e.lastSeenMessage.createdAt
            """)
    List<MessageEntity> findMinLastSeen(UUID roomId, String sender);
}