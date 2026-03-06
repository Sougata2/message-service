package com.domain.message_service.app.participants.repository;

import com.domain.message_service.app.participants.entity.ParticipantsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParticipantsRepository extends JpaRepository<ParticipantsEntity, Long> {
}