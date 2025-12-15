package com.victordev018.event_sync_backend.service;

import com.victordev018.event_sync_backend.domain.event.Event;
import com.victordev018.event_sync_backend.domain.subscription.Subscription;
import com.victordev018.event_sync_backend.domain.user.User;
import com.victordev018.event_sync_backend.domain.user.UserRole;
import com.victordev018.event_sync_backend.dto.event.EventDTO;
import com.victordev018.event_sync_backend.dto.event.EventResponseDTO;
import com.victordev018.event_sync_backend.exception.AlreadySubscribedException;
import com.victordev018.event_sync_backend.exception.CustomAuthenticationException;
import com.victordev018.event_sync_backend.exception.EventFullException;
import com.victordev018.event_sync_backend.exception.EventNotFoundException;
import com.victordev018.event_sync_backend.repository.EventRepository;
import com.victordev018.event_sync_backend.repository.SubscriptionRepository;
import com.victordev018.event_sync_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private UserRepository userRepository;

    public Event createEvent(EventDTO data) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Event newEvent = new Event(data.title(), data.description(), data.date(), data.location(), data.maxAttendees(), user);
        return eventRepository.save(newEvent);
    }

    public List<EventResponseDTO> getAllEvents() {
        return eventRepository.findAll().stream().map(event -> {
            Integer attendees = subscriptionRepository.countByEvent(event);
            return EventResponseDTO.fromEvent(event, attendees);
        }).toList();
    }

    public Event updateEvent(UUID id, EventDTO data) {
        Event event = eventRepository.findById(id).orElseThrow(() -> new EventNotFoundException("Event not found"));
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!event.getOrganizer().getId().equals(currentUser.getId()) && currentUser.getRole() != UserRole.ADMIN) {
            throw new CustomAuthenticationException("You don't have permission to update this event");
        }

        event.setTitle(data.title());
        event.setDescription(data.description());
        event.setDate(data.date());
        event.setLocation(data.location());
        event.setMaxAttendees(data.maxAttendees());

        return eventRepository.save(event);
    }

    public void deleteEvent(UUID id) {
        Event event = eventRepository.findById(id).orElseThrow(() -> new EventNotFoundException("Event not found"));
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!event.getOrganizer().getId().equals(currentUser.getId()) && currentUser.getRole() != UserRole.ADMIN) {
            throw new CustomAuthenticationException("You don't have permission to delete this event");
        }

        eventRepository.delete(event);
    }

    public void subscribe(UUID eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException("Event not found"));
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // Reload user to be sure
        currentUser = userRepository.findById(currentUser.getId()).orElseThrow(() -> new RuntimeException("User not found"));

        if (event.getOrganizer().getId().equals(currentUser.getId())) {
            throw new AlreadySubscribedException("Organizer cannot subscribe to their own event");
        }

        if (subscriptionRepository.findByEventAndUser(event, currentUser).isPresent()) {
            throw new AlreadySubscribedException("You are already subscribed to this event");
        }

        Integer attendeesCount = subscriptionRepository.countByEvent(event);
        if (attendeesCount >= event.getMaxAttendees()) {
            throw new EventFullException("Event is full");
        }

        Subscription subscription = new Subscription(currentUser, event, java.time.Instant.now());
        subscriptionRepository.save(subscription);
    }

    public void unsubscribe(UUID eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException("Event not found"));
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Subscription subscription = subscriptionRepository.findByEventAndUser(event, currentUser)
                .orElseThrow(() -> new AlreadySubscribedException("You are not subscribed to this event"));

        subscriptionRepository.delete(subscription);
    }

    public List<Event> getMyEvents() {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // Since we don't have a specific method in EventRepository yet, we might need one or just filter (not efficient but okay for now or assuming we add method)
        // Better: let's assume we can add findByOrganizer to EventRepository or just stream it if list is small. 
        // Strict requirement says: Return a list of events where the logged-in user is the organizer.
        // I will add findByOrganizer to Repository in a next step if I can't here.
        // Actually I can use: 
        return eventRepository.findAll().stream().filter(e -> e.getOrganizer().getId().equals(currentUser.getId())).toList();
    }

    public List<Event> getMySubscriptions() {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // Reload user to be sure attached to session
        currentUser = userRepository.findById(currentUser.getId()).orElseThrow(() -> new RuntimeException("User not found"));
        return subscriptionRepository.findAllEventsByUser(currentUser);
    }
}
