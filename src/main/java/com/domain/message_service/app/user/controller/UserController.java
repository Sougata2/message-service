package com.domain.message_service.app.user.controller;

import com.domain.message_service.app.user.dto.UserInfo;
import com.domain.message_service.app.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @GetMapping("/get-participant/{id}")
    public ResponseEntity<UserInfo> getParticipant(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserInfo(id));
    }

    @PostMapping("/update-last-seen")
    public ResponseEntity<Void> updateLastSeen(@RequestBody UserInfo userInfo) {
        userService.updateLastSeen(userInfo.lastSeen());
        return ResponseEntity.ok().build();
    }
}
