package com.victordev018.event_sync_backend.dto.event;

import java.time.Instant;
import java.util.UUID;

public record EventDTO(UUID id, String title, String description, Instant date, String location) {
}
