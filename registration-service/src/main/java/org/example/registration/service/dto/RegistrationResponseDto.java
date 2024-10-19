package org.example.registration.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationResponseDto {
    private Long id;
    private long eventId;
    private long participantId;
    private LocalDateTime registrationDate;
    private boolean isActive;
}