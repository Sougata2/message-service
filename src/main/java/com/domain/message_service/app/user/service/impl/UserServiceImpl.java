package com.domain.message_service.app.user.service.impl;


import com.domain.message_service.app.user.dto.UserInfo;
import com.domain.message_service.app.user.service.UserService;
import com.domain.message_service.client.auth.AuthClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
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
}
