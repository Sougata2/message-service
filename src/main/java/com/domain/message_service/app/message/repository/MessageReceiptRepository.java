package com.domain.message_service.app.message.repository;

import com.domain.message_service.app.message.entity.MessageReceiptEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MessageReceiptRepository extends JpaRepository<MessageReceiptEntity, Long> {
    Optional<MessageReceiptEntity> findByRoom_ReferenceNumberAndParticipant_Email(UUID roomReferenceNumber, String participantEmail);

    @Query("""
            select min(coalesce(e.lastReceivedMessage.id, 0))
            from MessageReceiptEntity e
            where e.room.referenceNumber = :roomId
            and e.participant.email <> :sender
            """)
    Long findMinimumLastReceived(UUID roomId, String sender);

    @Query("""
            select min(coalesce(e.lastSeenMessage.id, 0))
            from MessageReceiptEntity e
            where e.room.referenceNumber = :roomId
            and e.participant.email <> :sender
            """)
    Long findMinLastSeen(UUID roomId, String sender);
}