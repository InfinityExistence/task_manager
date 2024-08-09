package ru.effectivemobile.taskmanager.security.jwt;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record AccessToken(UUID id, String subject, List<String> authorities, Instant createdAt,
                          Instant expiresAt) implements Token {
}
