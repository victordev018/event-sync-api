package com.victordev018.event_sync_backend.exception;

import java.time.Instant;

public record StandardError(Instant timestamp, Integer status, String error, String message, String path) {
}
