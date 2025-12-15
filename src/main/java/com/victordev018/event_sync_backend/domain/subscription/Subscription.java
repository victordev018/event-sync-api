package com.victordev018.event_sync_backend.domain.subscription;

import com.victordev018.event_sync_backend.domain.event.Event;
import com.victordev018.event_sync_backend.domain.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Table(name = "subscriptions")
@Entity(name = "subscriptions")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    private Instant subscriptionTime;

    public Subscription(User user, Event event, Instant subscriptionTime) {
        this.user = user;
        this.event = event;
        this.subscriptionTime = subscriptionTime;
    }
}
