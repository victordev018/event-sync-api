package com.victordev018.event_sync_backend.dto.event;

import com.victordev018.event_sync_backend.domain.event.Event;

import java.time.Instant;
import java.util.UUID;

public record EventResponseDTO(UUID id, String title, String description, Instant date, String location, Integer maxAttendees, Integer attendeesCount) {
    public static EventResponseDTO fromEvent(Event event, Integer attendeesCount) {
        return new EventResponseDTO(event.getId(), event.getTitle(), event.getDescription(), event.getDate(), event.getLocation(), event.getMaxAttendees(), attendeesCount);
    }
}
