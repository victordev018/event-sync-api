package com.victordev018.event_sync_backend.repository;

import com.victordev018.event_sync_backend.domain.event.Event;
import com.victordev018.event_sync_backend.domain.subscription.Subscription;
import com.victordev018.event_sync_backend.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SubscriptionRepository extends JpaRepository<Subscription, UUID> {
    Optional<Subscription> findByEventAndUser(Event event, User user);
    
    @Query("SELECT s.event FROM subscriptions s WHERE s.user = :user")
    List<Event> findAllEventsByUser(@Param("user") User user);
    
    Integer countByEvent(Event event);
}
