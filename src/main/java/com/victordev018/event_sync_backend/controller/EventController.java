package com.victordev018.event_sync_backend.controller;

import com.victordev018.event_sync_backend.domain.event.Event;
import com.victordev018.event_sync_backend.dto.event.EventDTO;
import com.victordev018.event_sync_backend.service.EventService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/events")
public class EventController {

    @Autowired
    private EventService eventService;

    @PostMapping
    public ResponseEntity<Event> createEvent(@RequestBody @Valid EventDTO data){
        Event newEvent = this.eventService.createEvent(data);
        return ResponseEntity.ok(newEvent);
    }

    @GetMapping
    public ResponseEntity<List<Event>> getAllEvents(){
        List<Event> events = this.eventService.getAllEvents();
        return ResponseEntity.ok(events);
    }
}
