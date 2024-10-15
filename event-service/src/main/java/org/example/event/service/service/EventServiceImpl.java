package org.example.event.service.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.example.event.service.dto.event.EventMapper;
import org.example.event.service.dto.event.EventPartialUpdateDto;
import org.example.event.service.dto.event.EventRequestDto;
import org.example.event.service.dto.event.EventResponseDto;
import org.example.event.service.exception.InvalidEventDateRangeException;
import org.example.event.service.exception.InvalidEventStatusException;
import org.example.event.service.exception.ResourceNotFoundException;
import org.example.event.service.model.EventEntity;
import org.example.event.service.model.EventStatus;
import org.example.event.service.repository.EventRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;

    @Override
    public EventResponseDto createEvent(@NonNull EventRequestDto request) {
        //organisation validation

        // check dates of other events

        validateEventDates(request.getStartDate(), request.getEndDate());
        validateRegistrationDates(request.getRegistrationStartDate(), request.getRegistrationEndDate(), request.getStartDate());

        EventEntity event = getEventEntity(request);

        event = eventRepository.save(event);
        return eventMapper.toResponseDto(event);
    }

    @Override
    public EventResponseDto getEventById(@NonNull Long id) {
        EventEntity event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event with id = " + id + " was not found"));

        return eventMapper.toResponseDto(event);
    }

    @Override
    public List<EventResponseDto> getEvents(
            String name,
            LocalDateTime startDate,
            LocalDateTime endDate,
            EventStatus status,
            int page,
            int size
    ) {
        Pageable pageable = PageRequest.of(page, size);

        List<EventEntity> filteredEvents = eventRepository.getEvents(name, startDate, endDate, status, pageable);

        return filteredEvents.stream()
                .map(eventMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public int getAvailableSlotsCount(@NonNull Long id) {
        EventEntity event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event with id = " + id + " was not found"));

        return event.getAvailableSlots();
    }

    @Override
    public EventResponseDto updateEventPartially(@NonNull Long id, @NonNull EventPartialUpdateDto request) {
        EventEntity event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event with id = " + id + " was not found"));

        if (request.getName() != null && !request.getName().isBlank()) {
            event.setName(request.getName());
        }
        if (request.getDescription() != null && !request.getDescription().isBlank()) {
            event.setDescription(request.getDescription());
        }
        if (request.getStartDate() != null) {
            LocalDateTime endDate = request.getEndDate() != null ? request.getEndDate() : event.getEndDate();
            validateEventDates(request.getStartDate(), endDate);
            event.setStartDate(request.getStartDate());
        }
        if (request.getEndDate() != null) {
            validateEventDates(event.getStartDate(), request.getEndDate());
            event.setEndDate(request.getEndDate());
        }
        if (request.getRegistrationStartDate() != null) {
            LocalDateTime registrationEndDate = request.getRegistrationEndDate() != null ?
                    request.getRegistrationEndDate() : event.getRegistrationEndDate();
            validateRegistrationDates(request.getRegistrationStartDate(), registrationEndDate, event.getStartDate());
            event.setRegistrationStartDate(request.getRegistrationStartDate());
        }
        if (request.getRegistrationEndDate() != null) {
            validateRegistrationDates(event.getRegistrationStartDate(), request.getRegistrationEndDate(), event.getStartDate());
            event.setRegistrationEndDate(request.getRegistrationEndDate());
        }
        if (request.getMaxParticipants() != null && request.getMaxParticipants() > 0) {
            event.setMaxParticipants(request.getMaxParticipants());
            // checks available slots count
        }

        eventRepository.save(event);
        return eventMapper.toResponseDto(event);
    }

    @Override
    public EventResponseDto updateEventStatus(@NonNull Long id, @NonNull String status) {
        EventEntity event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event with id = " + id + " was not found"));

        EventStatus eventStatus = getEventStatus(status);
        event.setStatus(eventStatus);
        event = eventRepository.save(event);
        return eventMapper.toResponseDto(event);
    }


    private EventEntity getEventEntity(@NonNull EventRequestDto request) {
        EventEntity event = new EventEntity();
        event.setName(request.getName());
        event.setDescription(request.getDescription());
        event.setStartDate(request.getStartDate());
        event.setEndDate(request.getEndDate());
        event.setRegistrationStartDate(request.getRegistrationStartDate());
        event.setRegistrationEndDate(request.getRegistrationEndDate());
        event.setMaxParticipants(request.getMaxParticipants());
        event.setAvailableSlots(request.getMaxParticipants());
        return event;
    }

    private EventStatus getEventStatus(@NonNull String status) {
        try {
            return EventStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidEventStatusException(status);
        }
    }

    private void validateEventDates(@NonNull LocalDateTime startDate, @NonNull LocalDateTime endDate) {
        if (startDate.isAfter(endDate) || !startDate.isAfter(LocalDateTime.now())) {
            throw new InvalidEventDateRangeException("Event start date must be earlier than end date");
        }
    }

    private void validateRegistrationDates(
            @NonNull LocalDateTime registrationStartDate,
            @NonNull LocalDateTime registrationEndDate,
            @NonNull LocalDateTime eventStartDate
    ) {
        if (registrationStartDate.isAfter(registrationEndDate)) {
            throw new InvalidEventDateRangeException("Registration start date must be earlier than registration end date");
        }
        if (registrationEndDate.isAfter(eventStartDate)) {
            throw new InvalidEventDateRangeException("Registration end date must be earlier than event start date");
        }
    }
}