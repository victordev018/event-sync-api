package com.victordev018.event_sync_backend.controller;

import com.victordev018.event_sync_backend.domain.event.Event;
import com.victordev018.event_sync_backend.dto.event.EventDTO;
import com.victordev018.event_sync_backend.dto.event.EventResponseDTO;
import com.victordev018.event_sync_backend.service.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/events")
@Tag(name = "Events", description = "Management of events and subscriptions")
public class EventController {

    @Autowired
    private EventService eventService;

    @PostMapping
    @Operation(summary = "Create Event", description = "Creates a new event. Requires ADMIN role (or organizer role, depending on logic).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Created"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public ResponseEntity<Event> createEvent(@RequestBody @Valid EventDTO data){
        Event newEvent = this.eventService.createEvent(data);
        return ResponseEntity.ok(newEvent);
    }

    @GetMapping
    @Operation(summary = "List All Events", description = "Returns a list of all available events.")
    public ResponseEntity<List<EventResponseDTO>> getAllEvents(){
        List<EventResponseDTO> events = this.eventService.getAllEvents();
        return ResponseEntity.ok(events);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update Event", description = "Updates event details. Only the organizer or ADMIN can perform this.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated"),
            @ApiResponse(responseCode = "403", description = "Not the owner"),
            @ApiResponse(responseCode = "404", description = "Event not found")
    })
    public ResponseEntity<Event> updateEvent(@PathVariable UUID id, @RequestBody @Valid EventDTO data){
        Event updatedEvent = this.eventService.updateEvent(id, data);
        return ResponseEntity.ok(updatedEvent);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Event", description = "Cancels/Deletes an event.")
    public ResponseEntity<Void> deleteEvent(@PathVariable UUID id){
        this.eventService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/subscribe")
    @Operation(summary = "Subscribe to Event", description = "Registers the logged-in user to the event.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "409", description = "Event full or Already subscribed")
    })
    public ResponseEntity<Void> subscribe(@PathVariable UUID id){
        this.eventService.subscribe(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/subscribe")
    @Operation(summary = "Unsubscribe from Event", description = "Cancels the user's subscription to the event.")
    public ResponseEntity<Void> unsubscribe(@PathVariable UUID id){
        this.eventService.unsubscribe(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/my-events")
    @Operation(summary = "My Created Events", description = "Lists events organized by the current user.")
    public ResponseEntity<List<Event>> getMyEvents(){
        List<Event> events = this.eventService.getMyEvents();
        return ResponseEntity.ok(events);
    }

    @GetMapping("/attending")
    @Operation(summary = "My Subscriptions", description = "Lists events the current user is subscribed to.")
    public ResponseEntity<List<EventResponseDTO>> getMySubscriptions(){
        List<EventResponseDTO> events = this.eventService.getMySubscriptions();
        return ResponseEntity.ok(events);
    }

    @GetMapping("/{id}/subscriptions")
    @Operation(summary = "List Event Subscriptions", description = "Lists all participants of an event. Organizer only.")
    public ResponseEntity<List<com.victordev018.event_sync_backend.dto.subscription.SubscriptionDTO>> getEventSubscriptions(@PathVariable UUID id){
        var subscriptions = this.eventService.getSubscriptions(id);
        return ResponseEntity.ok(subscriptions);
    }

    @PostMapping("/{id}/checkin/{userId}")
    @Operation(summary = "Perform Check-in", description = "Marks a participant as present. Organizer only.")
    public ResponseEntity<Void> checkIn(@PathVariable UUID id, @PathVariable UUID userId){
        this.eventService.checkIn(id, userId);
        return ResponseEntity.ok().build();
    }
}
