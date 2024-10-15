package org.example.event.service.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.event.service.dto.event.EventPartialUpdateDto;
import org.example.event.service.dto.event.EventRequestDto;
import org.example.event.service.dto.event.EventResponseDto;
import org.example.event.service.model.EventStatus;
import org.example.event.service.service.EventService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @PostMapping
    public ResponseEntity<EventResponseDto> createEvent(@RequestBody @Valid EventRequestDto request) {
        EventResponseDto createdEvent = eventService.createEvent(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdEvent);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<EventResponseDto> updateEventPartially(
            @PathVariable Long id,
            @RequestBody EventPartialUpdateDto request
    ) {
        EventResponseDto updatedEvent = eventService.updateEventPartially(id, request);
        return ResponseEntity.ok(updatedEvent);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<EventResponseDto> updateEventStatus(
            @PathVariable Long id,
            @RequestBody String status
    ) {
        EventResponseDto updatedEvent = eventService.updateEventStatus(id, status);
        return ResponseEntity.ok(updatedEvent);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventResponseDto> getEventById(@PathVariable Long id) {
        EventResponseDto event = eventService.getEventById(id);
        return ResponseEntity.ok(event);
    }

    @GetMapping("/{id}/available-slots")
    public ResponseEntity<Integer> getAvailableSlotsCount(@PathVariable Long id) {
        Integer count = eventService.getAvailableSlotsCount(id);
        return ResponseEntity.ok(count);
    }

    @GetMapping
    public ResponseEntity<List<EventResponseDto>> getEvents(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) LocalDateTime startDate,
            @RequestParam(required = false) LocalDateTime endDate,
            @RequestParam(required = false) EventStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        List<EventResponseDto> events = eventService.getEvents(name, startDate, endDate, status, page, size);
        return ResponseEntity.ok(events);
    }
}