package com.domain.message_service.app.room.controller;

import com.domain.message_service.app.room.dto.RoomDto;
import com.domain.message_service.app.room.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rooms")
public class RoomController {
    private final RoomService service;

    @GetMapping("/reference/{number}")
    public ResponseEntity<RoomDto> findByNumber(@PathVariable UUID number) {
        return ResponseEntity.ok(service.findByReference(number));
    }

    @GetMapping("/subscribed-rooms")
    public ResponseEntity<List<RoomDto>> getSubscribedRooms() {
        return ResponseEntity.ok(service.getSubscribedRooms());
    }

    @GetMapping("/find-private-chat")
    public ResponseEntity<RoomDto> findPrivateRoomsByParticipants(@RequestParam Long participant) {
        return ResponseEntity.ok(service.findPrivateRoomByParticipants(participant));
    }

    @PostMapping("/new-group")
    public ResponseEntity<RoomDto> createGroup(@RequestBody RoomDto dto) {
        return ResponseEntity.ok(service.createGroup(dto));
    }

    @PostMapping("/new-private")
    public ResponseEntity<RoomDto> createPrivate(@RequestBody RoomDto dto) {
        return ResponseEntity.ok(service.createPrivate(dto));
    }

    @PutMapping
    public ResponseEntity<RoomDto> update(@RequestBody RoomDto dto) {
        return ResponseEntity.ok(service.update(dto));
    }

    @DeleteMapping
    public ResponseEntity<RoomDto> delete(@RequestBody RoomDto dto) {
        return ResponseEntity.ok(service.delete(dto));
    }
}
