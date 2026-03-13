package com.domain.message_service.app.file.repository;

import com.domain.message_service.app.file.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FileRepository extends JpaRepository<FileEntity, Long> {
    @Query("""
            select e from FileEntity e
            where e.message.room.referenceNumber = :roomId
            """)
    List<FileEntity> findByRoom(UUID roomId);

    @Query("""
            select e from FileEntity e
            where e.message.uuid = :uuid
            """)
    List<FileEntity> findByMessage(UUID uuid);

    @Modifying
    @Query(""" 
            delete from FileEntity e
            where e.message.uuid = :uuid
            """)
    void deleteByMessageUuid(UUID uuid);

    @Modifying
    @Query(""" 
            delete from FileEntity e
            where e.message.room.referenceNumber = :uuid
            """)
    void deleteByRoomUuid(UUID uuid);
}