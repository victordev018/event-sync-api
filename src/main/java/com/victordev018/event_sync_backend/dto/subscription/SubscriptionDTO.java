package com.victordev018.event_sync_backend.dto.subscription;

import com.victordev018.event_sync_backend.domain.subscription.Subscription;

import java.time.Instant;
import java.util.UUID;

public record SubscriptionDTO(UUID subscriptionId, UUID userId, String userName, String userEmail, Boolean checkedIn, Instant checkInTime) {
    public static SubscriptionDTO fromSubscription(Subscription s) {
        return new SubscriptionDTO(
            s.getId(),
            s.getUser().getId(),
            s.getUser().getName(),
            s.getUser().getEmail(),
            s.getCheckedIn(),
            s.getCheckInTime()
        );
    }
}
