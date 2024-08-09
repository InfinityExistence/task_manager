package ru.effectivemobile.taskmanager.security.jwt;

public record Tokens(String accessToken, String accessTokenExpiry, String refreshToken, String refreshTokenExpiry) {
}
