package com.victordev018.event_sync_backend.service;

import com.victordev018.event_sync_backend.domain.event.Event;
import com.victordev018.event_sync_backend.domain.user.User;
import com.victordev018.event_sync_backend.dto.event.EventDTO;
import com.victordev018.event_sync_backend.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    public Event createEvent(EventDTO data){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Event newEvent = new Event(data.title(), data.description(), data.date(), data.location(), user);
        return eventRepository.save(newEvent);
    }

    public List<Event> getAllEvents(){
        return eventRepository.findAll();
    }
}
