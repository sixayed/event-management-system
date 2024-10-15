package org.example.event.service.dto.event;

import org.example.event.service.model.EventEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface EventMapper {
    EventResponseDto toResponseDto(EventEntity event);
}
