package ru.effectivemobile.taskmanager.security.jwt.serialize;

import ru.effectivemobile.taskmanager.security.jwt.AccessToken;

import java.util.function.Function;

public interface AccessTokenJwsStringSerializer extends Function<AccessToken, String> {
}
