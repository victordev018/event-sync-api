package com.victordev018.event_sync_backend.dto.auth;

import com.victordev018.event_sync_backend.domain.user.UserRole;

public record RegisterDTO(String name, String email, String password, UserRole role) {
}
