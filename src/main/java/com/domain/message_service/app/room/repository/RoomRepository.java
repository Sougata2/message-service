package com.domain.message_service.app.room.repository;

import com.domain.message_service.app.room.entity.RoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoomRepository extends JpaRepository<RoomEntity, Long> {
    @Query("select e from RoomEntity e where e.referenceNumber = :reference")
    Optional<RoomEntity> findByReference(UUID reference);

    @Query("""
            select e from RoomEntity e
            right join e.participants f
            where f in :participants
            and e.type = com.domain.message_service.app.room.enums.Type.PRIVATE
            group by e.id
            having count(distinct f) = 2
            """)
    Optional<RoomEntity> findPrivateRoomsByParticipants(List<Long> participants);

    @Query("""
            select e from RoomEntity e
            right join e.participants f
            where f in :userId
            order by e.lastMessage.createdAt desc
            """)
    List<RoomEntity> getSubscribedRooms(Long userId);

    @Query("""
            select e.referenceNumber from RoomEntity e
            right join e.participants f
            where f in :userId
            order by e.lastMessage.createdAt desc
            """)
    List<UUID> getSubscribedRoomsHash(Long userId);
}