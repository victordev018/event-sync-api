package com.victordev018.event_sync_backend.repository;

import com.victordev018.event_sync_backend.domain.event.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EventRepository extends JpaRepository<Event, UUID> {
}
