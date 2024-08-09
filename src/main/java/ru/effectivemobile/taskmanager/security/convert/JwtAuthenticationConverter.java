package ru.effectivemobile.taskmanager.security.convert;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import ru.effectivemobile.taskmanager.security.jwt.deserialize.AccessTokenStringDeserializer;
import ru.effectivemobile.taskmanager.security.jwt.deserialize.RefreshTokenStringDeserializer;

@AllArgsConstructor
public class JwtAuthenticationConverter implements AuthenticationConverter {
    private final AccessTokenStringDeserializer accessTokenStringDeserializer;
    private final RefreshTokenStringDeserializer refreshTokenStringDeserializer;
    private final String bearer = "Bearer ";

    @Override
    public Authentication convert(HttpServletRequest request) {
        var authorization = request.getHeader("Authorization");
        if (hasAuthorizationAndBearerToken(authorization)) {
            var token = tokenWithoutBearer(authorization);
            return parseAccessOrRefreshToken(token);
        }
        return null;
    }

    private PreAuthenticatedAuthenticationToken parseAccessOrRefreshToken(String token) {

        var accessToken = accessTokenStringDeserializer.apply(token);
        if (accessToken != null)
            return new PreAuthenticatedAuthenticationToken(accessToken, token);

        var refreshToken = refreshTokenStringDeserializer.apply(token);
        if (refreshToken != null)
            return new PreAuthenticatedAuthenticationToken(refreshToken, token);
        return null;
    }

    private String tokenWithoutBearer(String authorization) {
        return authorization.substring(bearer.length());
    }

    private boolean hasAuthorizationAndBearerToken(String authorization) {
        return authorization != null && authorization.startsWith(bearer);
    }
}
