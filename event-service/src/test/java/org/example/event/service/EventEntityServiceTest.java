package org.example.event.service;

import org.example.event.service.dto.event.EventMapper;
import org.example.event.service.dto.event.EventPartialUpdateDto;
import org.example.event.service.dto.event.EventRequestDto;
import org.example.event.service.dto.event.EventResponseDto;
import org.example.event.service.exception.InvalidEventDateRangeException;
import org.example.event.service.model.EventEntity;
import org.example.event.service.model.EventStatus;
import org.example.event.service.repository.EventRepository;
import org.example.event.service.service.EventServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class EventEntityServiceTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private EventMapper eventMapper;

    @InjectMocks
    private EventServiceImpl eventService;

    private EventRequestDto eventRequestDto;
    private EventResponseDto eventResponseDto;
    private EventEntity event;

    private final String name = "event";
    private final String description = "description";

    @BeforeEach
    void setUp() {
        eventRequestDto = new EventRequestDto();
        eventRequestDto.setName(name);
        eventRequestDto.setDescription(description);
        eventRequestDto.setStartDate(LocalDateTime.now().plusDays(1));
        eventRequestDto.setEndDate(LocalDateTime.now().plusDays(2));
        eventRequestDto.setRegistrationStartDate(LocalDateTime.now());
        eventRequestDto.setRegistrationEndDate(LocalDateTime.now().plusHours(1));
        eventRequestDto.setMaxParticipants(100);

        event = new EventEntity();
        event.setId(1L);
        event.setName(name);
        event.setDescription(description);
        event.setStartDate(LocalDateTime.now().plusDays(1));
        event.setEndDate(LocalDateTime.now().plusDays(2));
        event.setRegistrationStartDate(LocalDateTime.now());
        event.setRegistrationEndDate(LocalDateTime.now().plusHours(1));
        event.setMaxParticipants(100);
        event.setAvailableSlots(100);

        eventResponseDto = new EventResponseDto();
        eventResponseDto.setId(1L);
        eventResponseDto.setName(name);
        eventResponseDto.setDescription(description);
        eventResponseDto.setMaxParticipants(100);
    }

    @Test
    void createEvent_ValidRequest_ReturnsEventResponse() {
        // arrange
        Mockito.when(eventRepository.save(any(EventEntity.class))).thenReturn(event);
        Mockito.when(eventMapper.toResponseDto(any(EventEntity.class))).thenReturn(eventResponseDto);

        // act
        EventResponseDto result = eventService.createEvent(eventRequestDto);

        // assert
        assertNotNull(result);
        assertEquals(eventResponseDto.getId(), result.getId());
        verify(eventRepository, times(1)).save(any(EventEntity.class));
    }

    @Test
    void getEventById_ValidId_ReturnsEventResponse() {
        // arrange
        Mockito.when(eventRepository.findById(anyLong())).thenReturn(Optional.of(event));
        Mockito.when(eventMapper.toResponseDto(any(EventEntity.class))).thenReturn(eventResponseDto);

        // act
        EventResponseDto result = eventService.getEventById(1L);

        // assert
        assertNotNull(result);
        assertEquals(eventResponseDto.getId(), result.getId());
        verify(eventRepository, times(1)).findById(1L);
    }

    @Test
    void getAvailableSlotsCount_ValidId_ReturnsSlotCount() {
        // arrange
        Mockito.when(eventRepository.findById(anyLong())).thenReturn(Optional.of(event));

        // act
        int availableSlots = eventService.getAvailableSlotsCount(1L);

        // assert
        assertEquals(100, availableSlots);
        verify(eventRepository, times(1)).findById(1L);
    }

    @Test
    void updateEventPartially_ValidRequest_UpdatesEvent() {
        // arrange
        String updatedDescription = "Updated Description";
        EventPartialUpdateDto partialUpdateDto = new EventPartialUpdateDto();
        partialUpdateDto.setDescription(updatedDescription);

        Mockito.when(eventRepository.findById(anyLong())).thenReturn(Optional.of(event));
        Mockito.when(eventRepository.save(any(EventEntity.class))).thenReturn(event);
        Mockito.when(eventMapper.toResponseDto(any(EventEntity.class))).thenReturn(eventResponseDto);

        // act
        EventResponseDto result = eventService.updateEventPartially(1L, partialUpdateDto);

        // assert
        assertNotNull(result);
        assertEquals(updatedDescription, event.getDescription());
        verify(eventRepository, times(1)).save(any(EventEntity.class));
    }

    @Test
    void updateEventStatus_ValidStatus_UpdatesEventStatus() {
        // arrange
        String newStatus = "CANCELED";
        event.setStatus(EventStatus.ACTIVE);

        Mockito.when(eventRepository.findById(anyLong())).thenReturn(Optional.of(event));
        Mockito.when(eventRepository.save(any(EventEntity.class))).thenReturn(event);
        Mockito.when(eventMapper.toResponseDto(any(EventEntity.class))).thenReturn(eventResponseDto);

        // act
        EventResponseDto result = eventService.updateEventStatus(1L, newStatus);

        // assert
        assertNotNull(result);
        assertEquals(EventStatus.CANCELED, event.getStatus());
        verify(eventRepository, times(1)).save(any(EventEntity.class));
    }

    @Test
    void createEvent_InvalidEventDates_ThrowsException() {
        // arrange
        eventRequestDto.setStartDate(LocalDateTime.now().plusDays(2));
        eventRequestDto.setEndDate(LocalDateTime.now().plusDays(1));

        // act & assert
        assertThrows(InvalidEventDateRangeException.class, () -> eventService.createEvent(eventRequestDto));
    }

    @Test
    void createEvent_InvalidRegistrationDates_ThrowsException() {
        // arrange
        eventRequestDto.setRegistrationStartDate(LocalDateTime.now().plusDays(2));
        eventRequestDto.setRegistrationEndDate(LocalDateTime.now().plusDays(1));

        // act & assert
        assertThrows(InvalidEventDateRangeException.class, () -> eventService.createEvent(eventRequestDto));
    }
}