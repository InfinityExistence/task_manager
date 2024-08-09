package ru.effectivemobile.taskmanager.security.jwt;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface Token {
    String subject();

    Instant expiresAt();

    UUID id();

    List<String> authorities();
}
