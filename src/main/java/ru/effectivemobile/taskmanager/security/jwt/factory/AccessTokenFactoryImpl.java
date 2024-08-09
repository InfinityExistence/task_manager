package ru.effectivemobile.taskmanager.security.jwt.factory;

import lombok.Setter;
import ru.effectivemobile.taskmanager.security.jwt.AccessToken;
import ru.effectivemobile.taskmanager.security.jwt.RefreshToken;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Setter
public class AccessTokenFactoryImpl implements AccessTokenFactory {
    private Duration tokenTtl = Duration.ofMinutes(10);

    @Override
    public AccessToken apply(RefreshToken token) {
        List<String> authorities = getGrantAuthority(token);
        var now = Instant.now();
        return new AccessToken(token.id(), token.subject(), authorities, now, now.plus(tokenTtl));
    }

    private List<String> getGrantAuthority(RefreshToken token) {
        List<String> authorities = new ArrayList<>();
        token.authorities().stream()
                .filter(authority -> authority.startsWith("GRANT_"))
                .forEach(authorities::add);
        return authorities;
    }
}
