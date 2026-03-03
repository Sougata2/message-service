package com.domain.message_service.app.user.controller;

import com.domain.message_service.app.user.dto.UserInfo;
import com.domain.message_service.app.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @GetMapping("/get-participant/{id}")
    public ResponseEntity<UserInfo> getParticipant(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserInfo(id));
    }
}
