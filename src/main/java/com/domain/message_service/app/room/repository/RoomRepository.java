package com.domain.message_service.app.room.repository;

import com.domain.message_service.app.room.entity.RoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoomRepository extends JpaRepository<RoomEntity, Long> {
    @Query("select e from RoomEntity e where e.referenceNumber = :reference")
    Optional<RoomEntity> findByReference(UUID reference);
}