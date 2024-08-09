package ru.effectivemobile.taskmanager.security.jwt.deserialize;

import ru.effectivemobile.taskmanager.security.jwt.AccessToken;

import java.util.function.Function;

public interface AccessTokenStringDeserializer extends Function<String, AccessToken> {
}
