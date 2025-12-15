package com.victordev018.event_sync_backend.dto.event;

import java.time.Instant;

public record EventDTO(String title, String description, Instant date, String location, Integer maxAttendees) {
}
