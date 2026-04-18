package com.domain.message_service.app.message.repository;

import com.domain.message_service.app.message.entity.MessageEntity;
import com.domain.message_service.app.message.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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

    @Modifying
    @Query("""
            update MessageEntity e
            set e.status = :status
            where e.uuid in :uuids
            """)
    void updateMessageStatus(List<UUID> uuids, Status status);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
            update MessageEntity e
            set e.status = :status
            where e.status <> :status
            and e.id <= :messageId
            and e.room.referenceNumber = :roomRef
            """)
    void updateMessageStatus(Long messageId, UUID roomRef, Status status);

    @Query("""
            select e
            from MessageEntity e
            where e.room.referenceNumber = :roomId
            and e.senderEmail <> :signedUser
            and e.id >= :fromId
            and e.id <= :toId
            """)
    List<MessageEntity> getMessageInRange(UUID roomId, String signedUser, Long fromId, Long toId);

    @Query("""
            select m
            from MessageEntity m
            join m.room r
            join r.participants p
            join MessageReceiptEntity mr
                on mr.room = r and mr.participant = p
            where p.email = :username and m.senderEmail <> :username
              and (
                    mr.lastSeenMessage is null\s
                    or m.id > mr.lastSeenMessage.id
                  )
            order by r.id, m.id
            """)
    List<MessageEntity> findAllUnreadMessages(String username);

    @Query("""
            select m
            from MessageEntity m
            join m.room r
            join r.participants p
            join MessageReceiptEntity mr
                on mr.room = r and mr.participant = p
            where p.email = :username and m.senderEmail <> :username
              and (
                    mr.lastReceivedMessage is null\s
                    or m.id > mr.lastReceivedMessage.id
                  )
            order by r.id, m.id
            """)
    List<MessageEntity> findAllUndeliveredMessages(String username);
}