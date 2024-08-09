package ru.effectivemobile.taskmanager.security.jwt.factory;

import org.springframework.security.core.Authentication;
import ru.effectivemobile.taskmanager.security.jwt.RefreshToken;

import java.util.function.Function;

public interface RefreshTokenFactory extends Function<Authentication, RefreshToken> {
}
