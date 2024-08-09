package ru.effectivemobile.taskmanager.security.jwt.serialize;

import ru.effectivemobile.taskmanager.security.jwt.RefreshToken;

import java.util.function.Function;

public interface RefreshTokenJweStringSerializer extends Function<RefreshToken, String> {
}
