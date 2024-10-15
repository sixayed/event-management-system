package org.example.event.service.repository;

import org.example.event.service.model.EventEntity;
import org.example.event.service.model.EventStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<EventEntity, Long> {
    @Query("SELECT e FROM EventEntity e WHERE "
            + "(:name IS NULL OR LOWER(e.name) LIKE LOWER(CONCAT('%', :name, '%'))) "
            + "AND (:startDate IS NULL OR e.startDate >= :startDate) "
            + "AND (:endDate IS NULL OR e.endDate <= :endDate) "
            + "AND (:status IS NULL OR e.status = :status) ")
    List<EventEntity> getEvents(
            @Param("name") String name,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("status") EventStatus status,
            Pageable pageable
    );
}
