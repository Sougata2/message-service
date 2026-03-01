package com.domain.message_service.app.user.dto;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/*
 * User projection
 * */
@Getter
@Setter
@MappedSuperclass
@RequiredArgsConstructor
public abstract class UserFields {
    @Column
    private Long senderId;
    @Column
    private String senderEmail;
    @Column
    private String senderFirstName;
    @Column
    private String senderLastName;
}
