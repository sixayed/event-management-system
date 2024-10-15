package org.example.event.service;

import org.example.event.service.model.EventEntity;
import org.example.event.service.model.EventStatus;
import org.example.event.service.repository.EventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class EventRepositoryTest {

    @Autowired
    private EventRepository eventRepository;

    private EventEntity firstEvent;
    private EventEntity secondEvent;
    private EventEntity thirdEvent;

    private final String firstName = "First";
    private final String secondName = "Second";
    private final String thirdName = "Third";

    private final LocalDateTime date = LocalDateTime.of(2024, 10, 15, 0, 0, 0, 0);

    @BeforeEach
    void setUp() {
        firstEvent = new EventEntity();
        firstEvent.setName(firstName);
        firstEvent.setStartDate(date.plusDays(1));
        firstEvent.setEndDate(date.plusDays(2));
        firstEvent.setStatus(EventStatus.ACTIVE);
        eventRepository.save(firstEvent);

        secondEvent = new EventEntity();
        secondEvent.setName(secondName);
        secondEvent.setStartDate(date.plusDays(3));
        secondEvent.setEndDate(date.plusDays(4));
        secondEvent.setStatus(EventStatus.CANCELED);
        eventRepository.save(secondEvent);

        thirdEvent = new EventEntity();
        thirdEvent.setName(thirdName);
        thirdEvent.setStartDate(date.plusDays(5));
        thirdEvent.setEndDate(date.plusDays(6));
        thirdEvent.setStatus(EventStatus.ACTIVE);
        eventRepository.save(thirdEvent);
    }

    @Test
    void getEvents_NoFilters_ReturnsAllEvents() {
        // act
        List<EventEntity> result = eventRepository.getEvents(
                null,
                null,
                null,
                null,
                Pageable.unpaged()
        );

        // assert
        assertEquals(3, result.size());
    }

    @Test
    void getEvents_FilterByName_ReturnsMatchingEvents() {
        // act
        List<EventEntity> result = eventRepository.getEvents(
                firstName,
                null,
                null,
                null,
                Pageable.unpaged()
        );

        // assert
        assertEquals(1, result.size());
        assertEquals(firstName, result.get(0).getName());
    }

    @Test
    void getEvents_FilterByStartDate_ReturnsEventsOnOrAfterStartDate() {
        // act
        List<EventEntity> result = eventRepository.getEvents(
                null,
                date.plusDays(2),
                null,
                null,
                Pageable.unpaged()
        );

        // assert
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(event -> event.getStartDate().isAfter(date.plusDays(2).minusSeconds(1))));
    }

    @Test
    void getEvents_FilterByEndDate_ReturnsEventsOnOrBeforeEndDate() {
        // act
        List<EventEntity> result = eventRepository.getEvents(
                null,
                null,
                date.plusDays(4),
                null,
                Pageable.unpaged()
        );

        // assert
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(event -> event.getEndDate().isBefore(date.plusDays(4).plusSeconds(1))));
    }

    @Test
    void getEvents_FilterByStatus_ReturnsMatchingEvents() {
        // arrange
        EventStatus status = EventStatus.ACTIVE;

        // act
        List<EventEntity> result = eventRepository.getEvents(
                null,
                null,
                null,
                status,
                Pageable.unpaged()
        );

        // assert
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(event -> event.getStatus() == status));
    }

    @Test
    void getEvents_CombinationOfFilters_ReturnsMatchingEvents() {
        // arrange
        EventStatus status = EventStatus.ACTIVE;

        // act
        List<EventEntity> result = eventRepository.getEvents(
                firstName,
                date,
                date.plusDays(3),
                status,
                Pageable.unpaged()
        );

        // assert
        assertEquals(1, result.size());
        assertEquals(firstName, result.get(0).getName());
        assertEquals(status, result.get(0).getStatus());
    }

    @Test
    void getEvents_WithPagination_ReturnsCorrectPage() {
        // arrange
        Pageable pageable = PageRequest.of(0, 2);

        // act
        List<EventEntity> result = eventRepository.getEvents(
                null,
                null,
                null,
                null,
                pageable
        );

        // assert
        assertEquals(2, result.size());
    }
}