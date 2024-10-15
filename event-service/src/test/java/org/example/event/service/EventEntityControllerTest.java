package org.example.event.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.event.service.controller.EventController;
import org.example.event.service.dto.event.EventPartialUpdateDto;
import org.example.event.service.dto.event.EventRequestDto;
import org.example.event.service.dto.event.EventResponseDto;
import org.example.event.service.model.EventStatus;
import org.example.event.service.service.EventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EventController.class)
public class EventEntityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EventService eventService;

    @Autowired
    private ObjectMapper objectMapper;

    private EventRequestDto eventRequestDto;
    private EventResponseDto eventResponseDto;
    private EventPartialUpdateDto eventPartialUpdateDto;

    @BeforeEach
    void setUp() {
        eventRequestDto = new EventRequestDto();
        eventRequestDto.setName("Sample EventEntity");
        eventRequestDto.setDescription("Sample Description");
        eventRequestDto.setStartDate(LocalDateTime.now());
        eventRequestDto.setEndDate(LocalDateTime.now().plusDays(1));
        eventRequestDto.setRegistrationStartDate(LocalDateTime.now());
        eventRequestDto.setRegistrationEndDate(LocalDateTime.now().plusDays(1));
        eventRequestDto.setMaxParticipants(100);

        eventResponseDto = new EventResponseDto();
        eventResponseDto.setId(1L);
        eventResponseDto.setName("Sample EventEntity");
        eventResponseDto.setStatus(EventStatus.ACTIVE);
        eventResponseDto.setDescription("Sample Description");
        eventResponseDto.setMaxParticipants(100);

        eventPartialUpdateDto = new EventPartialUpdateDto();
        eventPartialUpdateDto.setDescription("Updated Description");
    }

    @Test
    void createEvent_ValidRequest_CreatedStatus() throws Exception {
        // arrange
        Mockito.when(eventService.createEvent(any(EventRequestDto.class))).thenReturn(eventResponseDto);

        // act & assert
        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(eventRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(eventResponseDto.getId()));
    }

    @Test
    void updateEventPartially_ValidRequest_UpdatedEvent() throws Exception {
        //arrange
        Mockito.when(eventService.updateEventPartially(anyLong(), any(EventPartialUpdateDto.class)))
                .thenReturn(eventResponseDto);

        // act & assert
        mockMvc.perform(patch("/api/events/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(eventPartialUpdateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value(eventResponseDto.getDescription()));
    }

    @Test
    void updateEventStatus_ValidStatus_UpdatedEvent() throws Exception {
        // arrange
        String status = "ACTIVE";
        Mockito.when(eventService.updateEventStatus(anyLong(), any(String.class)))
                .thenReturn(eventResponseDto);


        // act & assert
        mockMvc.perform(patch("/api/events/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(status)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(status));
    }

    @Test
    void getEventById_ValidId_ReturnsEvent() throws Exception {
        // arrange
        Mockito.when(eventService.getEventById(anyLong())).thenReturn(eventResponseDto);

        // act & assert
        mockMvc.perform(get("/api/events/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(eventResponseDto.getId()));
    }

    @Test
    void getAvailableSlotsCount_ValidId_ReturnsSlotCount() throws Exception {
        // arrange
        int slotsCount = 50;
        Mockito.when(eventService.getAvailableSlotsCount(anyLong())).thenReturn(slotsCount);

        // act & assert
        mockMvc.perform(get("/api/events/1/available-slots")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(slotsCount));
    }

    @Test
    void getEvents_ValidRequest_ReturnsEventsList() throws Exception {
        // arrange
        Mockito.when(eventService.getEvents(any(), any(), any(), any(), anyInt(), anyInt()))
                .thenReturn(Collections.singletonList(eventResponseDto));

        // act & assert
        mockMvc.perform(get("/api/events")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(eventResponseDto.getId()));
    }
}
