package ru.effectivemobile.taskmanager.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import ru.effectivemobile.taskmanager.entity.TokenUser;
import ru.effectivemobile.taskmanager.repository.DeactivatedTokenRepository;
import ru.effectivemobile.taskmanager.security.jwt.RefreshToken;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TokenAuthenticationUserDetailsServiceTest {
    @Mock
    private DeactivatedTokenRepository deactivatedTokenRepository;

    @InjectMocks
    private TokenAuthenticationUserDetailsService tokenAuthenticationUserDetailsService;


    @Test
    @DisplayName("Успешная загрузка данных пользователя с валидным токеном")
    void testLoadUserDetails_ValidToken_Success() {
        RefreshToken token = new RefreshToken(UUID.randomUUID(), "user@mail.ru", List.of("JWT_REFRESH"), Instant.now(), Instant.now().plusSeconds(3600));
        PreAuthenticatedAuthenticationToken authenticationToken = new PreAuthenticatedAuthenticationToken(token, null);

        when(deactivatedTokenRepository.existsById(token.id())).thenReturn(false);

        TokenUser userDetails = (TokenUser) tokenAuthenticationUserDetailsService.loadUserDetails(authenticationToken);

        assertNotNull(userDetails);
        assertEquals("user@mail.ru", userDetails.getUsername());
        assertEquals("nopassword", userDetails.getPassword());
        assertTrue(userDetails.isEnabled());
        assertTrue(userDetails.isAccountNonExpired());
        assertTrue(userDetails.isCredentialsNonExpired());
        assertTrue(userDetails.isAccountNonLocked());
        assertEquals(Set.of(new SimpleGrantedAuthority("JWT_REFRESH")), userDetails.getAuthorities());
        assertEquals(token, userDetails.getToken());
    }


    @Test
    @DisplayName("Загрузка данных пользователя с невалидным типом токена. Выброс исключения")
    void testLoadUserDetails_InvalidTokenType_ThrowsUsernameNotFoundException() {
        PreAuthenticatedAuthenticationToken authenticationToken = new PreAuthenticatedAuthenticationToken("invalidToken", null);

        assertThrows(UsernameNotFoundException.class, () ->
                tokenAuthenticationUserDetailsService.loadUserDetails(authenticationToken));
    }

}