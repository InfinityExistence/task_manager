package ru.effectivemobile.taskmanager.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Setter;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.effectivemobile.taskmanager.exception.AuthenticationException;
import ru.effectivemobile.taskmanager.security.jwt.AccessToken;
import ru.effectivemobile.taskmanager.security.jwt.RefreshToken;
import ru.effectivemobile.taskmanager.security.jwt.Tokens;
import ru.effectivemobile.taskmanager.security.jwt.factory.AccessTokenFactory;
import ru.effectivemobile.taskmanager.security.jwt.factory.AccessTokenFactoryImpl;
import ru.effectivemobile.taskmanager.security.jwt.factory.RefreshTokenFactory;
import ru.effectivemobile.taskmanager.security.jwt.factory.RefreshTokenFactoryImpl;
import ru.effectivemobile.taskmanager.security.jwt.serialize.AccessTokenJwsStringSerializer;
import ru.effectivemobile.taskmanager.security.jwt.serialize.RefreshTokenJweStringSerializer;

import java.io.IOException;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Setter
public class RequestJwtTokensFilter extends OncePerRequestFilter {
    private RequestMatcher requestMatcher = new AntPathRequestMatcher("/api/auth/login", HttpMethod.POST.name());
    private SecurityContextRepository securityContextRepository = new RequestAttributeSecurityContextRepository();
    private AccessTokenFactory accessTokenFactory = new AccessTokenFactoryImpl();
    private RefreshTokenFactory refreshTokenFactory = new RefreshTokenFactoryImpl();
    private AccessTokenJwsStringSerializer accessTokenJwsStringSerializer;
    private RefreshTokenJweStringSerializer refreshTokenJweStringSerializer;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (requestMatcher.matches(request)) {
            if (securityContextRepository.containsContext(request)) {
                var context = securityContextRepository.loadDeferredContext(request).get();
                if (isContextExistsAndNotPreAuthenticated(context)) {
                    var refreshToken = refreshTokenFactory.apply(context.getAuthentication());
                    var accessToken = this.accessTokenFactory.apply(refreshToken);
                    sendTokens(response, accessToken, refreshToken);
                    return;
                }
            }

            throw new AuthenticationException("User must be authenticated");
        }
        filterChain.doFilter(request, response);
    }

    private boolean isContextExistsAndNotPreAuthenticated(SecurityContext context) {
        return context != null && !(context.getAuthentication() instanceof PreAuthenticatedAuthenticationToken);
    }

    private void sendTokens(HttpServletResponse response, AccessToken accessToken, RefreshToken refreshToken) throws IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(APPLICATION_JSON_VALUE);
        objectMapper.writeValue(response.getWriter(),
                new Tokens(accessTokenJwsStringSerializer.apply(accessToken),
                        accessToken.expiresAt().toString(),
                        refreshTokenJweStringSerializer.apply(refreshToken),
                        refreshToken.expiresAt().toString()));
    }
}
