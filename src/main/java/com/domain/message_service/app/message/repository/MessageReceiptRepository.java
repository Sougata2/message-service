package com.domain.message_service.app.message.repository;

import com.domain.message_service.app.message.dto.ReadReceiptDto;
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

    @Query("""
            select new com.domain.message_service.app.message.dto.ReadReceiptDto(
                count(f),
                e.room.referenceNumber,
                e.lastSeenMessage.uuid
            )
            from MessageReceiptEntity e
            join MessageEntity f
                on f.room.id = e.room.id
            where f.id > e.lastSeenMessage.id
            and f.senderEmail <> :email
            and e.participant.email = :email
            group by e.lastSeenMessage.uuid, e.room.referenceNumber
            """)
    List<ReadReceiptDto> getReadReceipts(String email);
}