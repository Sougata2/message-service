package com.domain.message_service.app.message.repository;

import com.domain.message_service.app.message.entity.MessageReceiptEntity;
import com.domain.message_service.app.message.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MessageReceiptRepository extends JpaRepository<MessageReceiptEntity, Long> {
    @Modifying
    @Query("""
            update MessageReceiptEntity e
            set
                e.lastReceivedMessage =
                case when :status = 'DELIVERED' then :messageId end,
                e.lastSeenMessage =
                case when :status = 'READ' then :messageId end
            where e.participant.email = :username and e.room.referenceNumber = :roomRef
            """)
    void updateMessageReceipt(UUID messageId, Status status, String username, UUID roomRef);

    @Modifying
    @Query("""
            update MessageReceiptEntity e
            set e.lastReceivedMessage = :uuid
            where e.room.referenceNumber = :roomRef and e.participant.email = :username
            """)
    void updateLastReceived(UUID messageId, String username, UUID roomRef);

    @Modifying
    @Query("""
            update MessageReceiptEntity e
            set e.lastSeenMessage = :uuid
            where e.room.referenceNumber = :roomRef and e.participant.email = :username
            """)
    void updateLastSeen(UUID messageId, String username, UUID roomRef);
}