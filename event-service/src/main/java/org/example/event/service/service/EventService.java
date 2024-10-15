package org.example.event.service.service;

import org.example.event.service.dto.event.EventPartialUpdateDto;
import org.example.event.service.dto.event.EventRequestDto;
import org.example.event.service.dto.event.EventResponseDto;
import org.example.event.service.model.EventStatus;

import java.time.LocalDateTime;
import java.util.List;


public interface EventService {
    EventResponseDto createEvent(EventRequestDto request);

    EventResponseDto getEventById(Long id);

    List<EventResponseDto> getEvents(String name, LocalDateTime startDate, LocalDateTime endDate, EventStatus status, int page, int size);

    int getAvailableSlotsCount(Long id);

    EventResponseDto updateEventPartially(Long id, EventPartialUpdateDto request);

    EventResponseDto updateEventStatus(Long id, String status);
}
