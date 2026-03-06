package com.domain.message_service.client.auth;

import com.domain.message_service.app.user.dto.UserInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "auth-service")
public interface AuthClient {
    @GetMapping("/users/user-info/{email}")
    UserInfo getUserInfo(@PathVariable String email);

    @GetMapping("/users/id/{id}")
    UserInfo getUserById(@PathVariable Long id);

    @GetMapping("/users/ids")
    List<UserInfo> getUsersByIds(@RequestParam List<Long> ids);
}
