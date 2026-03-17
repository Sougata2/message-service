package com.domain.message_service.app.user.service.impl;


import com.domain.message_service.app.participants.entity.ParticipantsEntity;
import com.domain.message_service.app.participants.repository.ParticipantsRepository;
import com.domain.message_service.app.user.dto.UserInfo;
import com.domain.message_service.app.user.service.UserService;
import com.domain.message_service.client.auth.AuthClient;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final ParticipantsRepository participantsRepository;
    private final AuthClient authClient;

    @Override
    public UserInfo getUserInfo(Long id) {
        UserInfo userInfo;
        try {
            userInfo = authClient.getUserById(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return userInfo;
    }

    @Override
    public void updateLastSeen(Long lastSeen) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        ParticipantsEntity participant = participantsRepository.findByEmail(username)
                .orElseThrow(() -> new EntityNotFoundException("Participant %s is not found".formatted(username)));
        participant.setLastSeen(lastSeen);
        participantsRepository.save(participant);
    }
}
