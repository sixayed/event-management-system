package org.example.event.service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity()
@Table(name = "events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    @ManyToOne
    @JoinColumn(name = "organization_id")
    private OrganizationEntity organization;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "registation_start_date")
    private LocalDateTime registrationStartDate;

    @Column(name = "registation_end_date")
    private LocalDateTime registrationEndDate;

    @Column(name = "max_participants")
    private Integer maxParticipants;

    @Column(name = "available_slots")
    private Integer availableSlots;

    @Enumerated(EnumType.STRING)
    private EventStatus status = EventStatus.ACTIVE;
}