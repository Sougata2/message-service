package com.domain.message_service.app.participants.entity;

import com.domain.message_service.app.message.entity.MessageReceiptEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "participants")
public class ParticipantsEntity {
    @Id
    @Column
    private Long id;

    @Column
    private String email;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column
    private Long lastSeen;

    /*
     * RELATIONS
     * */
    @OneToMany(mappedBy = "participant", orphanRemoval = true)
    private List<MessageReceiptEntity> messageReceipts;
}
