package com.domain.message_service.app.message.controller;

import com.domain.message_service.app.message.dto.MessageDto;
import com.domain.message_service.app.message.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/messages")
public class MessageController {
    private final MessageService service;

    @GetMapping("/room/{number}")
    public ResponseEntity<List<MessageDto>> findByRoom(@PathVariable("number") UUID roomId) {
        return ResponseEntity.ok(service.findByRoom(roomId));
    }

    @PostMapping
    public ResponseEntity<MessageDto> save(@RequestBody MessageDto dto) {
        return ResponseEntity.ok(service.save(dto));
    }

    @PutMapping
    public ResponseEntity<MessageDto> update(@RequestBody MessageDto dto) {
        return ResponseEntity.ok(service.update(dto));
    }

    @DeleteMapping
    public ResponseEntity<MessageDto> delete(@RequestBody MessageDto dto) {
        return ResponseEntity.ok(service.delete(dto));
    }
}
