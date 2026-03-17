package com.domain.message_service.app.user.service;

import com.domain.message_service.app.user.dto.UserInfo;

public interface UserService {
    UserInfo getUserInfo(Long id);

    void updateLastSeen(Long lastSeen);
}
