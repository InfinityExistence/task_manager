package ru.effectivemobile.taskmanager.security.jwt.factory;

import ru.effectivemobile.taskmanager.security.jwt.AccessToken;
import ru.effectivemobile.taskmanager.security.jwt.RefreshToken;

import java.util.function.Function;

public interface AccessTokenFactory extends Function<RefreshToken, AccessToken> {
}
