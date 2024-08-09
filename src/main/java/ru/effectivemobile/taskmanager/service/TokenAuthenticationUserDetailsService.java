package ru.effectivemobile.taskmanager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Service;
import ru.effectivemobile.taskmanager.entity.TokenUser;
import ru.effectivemobile.taskmanager.repository.DeactivatedTokenRepository;
import ru.effectivemobile.taskmanager.security.jwt.Token;

import java.time.Instant;
import java.util.List;

@Service
public class TokenAuthenticationUserDetailsService implements AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> {

    @Autowired
    DeactivatedTokenRepository deactivatedTokenRepository;

    @Override
    public UserDetails loadUserDetails(PreAuthenticatedAuthenticationToken authenticationToken) throws UsernameNotFoundException {
        if (authenticationToken.getPrincipal() instanceof Token token) {
            return new TokenUser(token.subject(),
                    "nopassword",
                    true, true,
                    isCredentialsNonExpired(token),
                    true,
                    getSimpleGrantedAuthorities(token),
                    token);

        }
        throw new UsernameNotFoundException("Principal must be of type Token");
    }

    private List<SimpleGrantedAuthority> getSimpleGrantedAuthorities(Token token) {
        return token.authorities().stream().map(SimpleGrantedAuthority::new).toList();
    }

    private boolean isCredentialsNonExpired(Token token) {
        return token.expiresAt().isAfter(Instant.now()) &&
                !deactivatedTokenRepository.existsById(token.id());
    }
}
