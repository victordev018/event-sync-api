package com.victordev018.event_sync_backend.dto.auth;

import java.util.UUID;

public record LoginResponseDTO(String token, UUID userId, String name, String role) {
}
