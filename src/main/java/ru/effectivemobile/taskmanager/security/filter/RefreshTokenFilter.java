package ru.effectivemobile.taskmanager.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Setter;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.effectivemobile.taskmanager.entity.TokenUser;
import ru.effectivemobile.taskmanager.exception.AuthenticationException;
import ru.effectivemobile.taskmanager.security.jwt.AccessToken;
import ru.effectivemobile.taskmanager.security.jwt.RefreshToken;
import ru.effectivemobile.taskmanager.security.jwt.Tokens;
import ru.effectivemobile.taskmanager.security.jwt.factory.AccessTokenFactory;
import ru.effectivemobile.taskmanager.security.jwt.factory.AccessTokenFactoryImpl;
import ru.effectivemobile.taskmanager.security.jwt.serialize.AccessTokenJwsStringSerializer;

import java.io.IOException;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Setter
public class RefreshTokenFilter extends OncePerRequestFilter {
    private RequestMatcher requestMatcher = new AntPathRequestMatcher("/api/auth/jwt/refresh", HttpMethod.POST.name());
    private SecurityContextRepository securityContextRepository = new RequestAttributeSecurityContextRepository();
    private AccessTokenFactory accessTokenFactory = new AccessTokenFactoryImpl();
    private AccessTokenJwsStringSerializer accessTokenJwsStringSerializer;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (requestMatcher.matches(request)) {
            if (this.securityContextRepository.containsContext(request)) {
                var context = securityContextRepository.loadDeferredContext(request).get();
                var refreshToken = getRefreshToken(context);
                if (refreshToken != null) {
                    var accessToken = accessTokenFactory.apply(refreshToken);
                    sendAccessToken(response, accessToken);
                    return;
                }


            }

            throw new AuthenticationException("User must be authenticated with refresh JWT");
        }
        filterChain.doFilter(request, response);
    }

    private void sendAccessToken(HttpServletResponse response, AccessToken accessToken) throws IOException {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(APPLICATION_JSON_VALUE);
        objectMapper.writeValue(response.getWriter(), new Tokens(accessTokenJwsStringSerializer.apply(accessToken),
                accessToken.expiresAt().toString(),
                null, null));
    }

    private RefreshToken getRefreshToken(SecurityContext context) {
        if (context != null && context.getAuthentication() instanceof PreAuthenticatedAuthenticationToken &&
                context.getAuthentication().getPrincipal() instanceof TokenUser user &&
                context.getAuthentication().getAuthorities().contains(new SimpleGrantedAuthority("JWT_REFRESH")) &&
                user.getToken() instanceof RefreshToken refreshToken)
            return refreshToken;
        return null;

    }

}
