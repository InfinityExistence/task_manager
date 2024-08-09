package ru.effectivemobile.taskmanager.security.jwt.factory;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import ru.effectivemobile.taskmanager.security.jwt.RefreshToken;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RefreshTokenFactoryImpl implements RefreshTokenFactory {
    private final Duration tokenTtl = Duration.ofDays(1);

    @Override
    public RefreshToken apply(Authentication authentication) {
        List<String> authorities = getRefreshAuthorities(authentication);
        var now = Instant.now();
        return getRefreshToken(authentication, authorities, now);
    }

    private RefreshToken getRefreshToken(Authentication authentication, List<String> authorities, Instant now) {
        return new RefreshToken(UUID.randomUUID(), authentication.getName(), authorities, now, now.plus(tokenTtl));
    }

    private List<String> getRefreshAuthorities(Authentication authentication) {
        List<String> authorities = new ArrayList<>();
        authorities.add("JWT_REFRESH");
        authorities.add("JWT_LOGOUT");
        authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .map(authority -> "GRANT_" + authority)
                .forEach(authorities::add);
        return authorities;
    }
}
