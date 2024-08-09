package ru.effectivemobile.taskmanager.entity;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import ru.effectivemobile.taskmanager.security.jwt.Token;

import java.util.Collection;

@Getter
public class TokenUser extends User {
    final private Token token;

    public TokenUser(String username, String password, Collection<? extends GrantedAuthority> authorities, Token token) {
        super(username, password, authorities);
        this.token = token;
    }

    public TokenUser(String username, String password, boolean enabled,
                     boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked,
                     Collection<? extends GrantedAuthority> authorities, Token token) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.token = token;

    }
}
