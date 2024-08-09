package ru.effectivemobile.taskmanager.security.jwt.deserialize;

import ru.effectivemobile.taskmanager.security.jwt.RefreshToken;

import java.util.function.Function;

public interface RefreshTokenStringDeserializer extends Function<String, RefreshToken> {
}
