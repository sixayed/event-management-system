package org.example.event.service.dto.event;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventRequestDto {
    @NotBlank
    @Size(max = 70)
    private String name;

    @NotBlank
    @Size(max = 300)
    private String description;

    @NotNull
    private LocalDateTime startDate;

    @NotNull
    private LocalDateTime endDate;

    @NotNull
    private LocalDateTime registrationStartDate;

    @NotNull
    private LocalDateTime registrationEndDate;

    @Min(1)
    @NotNull
    private Integer maxParticipants;
}
