package com.domain.message_service.app.user.dto;

public record UserInfo(
        Long id,
        String firstName,
        String lastName,
        String email
) {
}
