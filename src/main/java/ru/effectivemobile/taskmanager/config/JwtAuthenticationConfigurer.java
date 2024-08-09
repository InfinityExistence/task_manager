package ru.effectivemobile.taskmanager.config;

import lombok.Builder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import ru.effectivemobile.taskmanager.exception.AuthenticationGlobalExceptionHandler;
import ru.effectivemobile.taskmanager.security.convert.JwtAuthenticationConverter;
import ru.effectivemobile.taskmanager.security.filter.RefreshTokenFilter;
import ru.effectivemobile.taskmanager.security.filter.RequestJwtTokensFilter;
import ru.effectivemobile.taskmanager.security.jwt.deserialize.AccessTokenStringDeserializer;
import ru.effectivemobile.taskmanager.security.jwt.deserialize.RefreshTokenStringDeserializer;
import ru.effectivemobile.taskmanager.security.jwt.serialize.AccessTokenJwsStringSerializerImpl;
import ru.effectivemobile.taskmanager.security.jwt.serialize.RefreshTokenJweStringSerializerImpl;
import ru.effectivemobile.taskmanager.service.TokenAuthenticationUserDetailsService;

@Builder
public class JwtAuthenticationConfigurer extends AbstractHttpConfigurer<JwtAuthenticationConfigurer, HttpSecurity> {
    private AccessTokenJwsStringSerializerImpl accessTokenJwsStringSerializer;
    private RefreshTokenJweStringSerializerImpl refreshTokenJweStringSerializer;
    private AccessTokenStringDeserializer accessTokenStringDeserializer;
    private RefreshTokenStringDeserializer refreshTokenStringDeserializer;
    private TokenAuthenticationUserDetailsService tokenAuthenticationUserDetailsService;

    @Override
    public void configure(HttpSecurity builder) {
        RequestJwtTokensFilter jwtRequestFilter = new RequestJwtTokensFilter();
        jwtRequestFilter.setAccessTokenJwsStringSerializer(accessTokenJwsStringSerializer);
        jwtRequestFilter.setRefreshTokenJweStringSerializer(refreshTokenJweStringSerializer);

        var jwtAuthenticationFilter = getJwtAuthenticationFilter(builder);

        var refreshTokenFilter = getRefreshTokenFilter();

        var authenticationProvider = getPreAuthenticatedAuthenticationProvider();

        builder.addFilterAfter(jwtAuthenticationFilter, LogoutFilter.class)
                .addFilterBefore(jwtRequestFilter, AnonymousAuthenticationFilter.class)
                .addFilterBefore(refreshTokenFilter, ExceptionTranslationFilter.class)
                .authenticationProvider(authenticationProvider);
    }

    private PreAuthenticatedAuthenticationProvider getPreAuthenticatedAuthenticationProvider() {
        var authenticationProvider = new PreAuthenticatedAuthenticationProvider();
        authenticationProvider.setPreAuthenticatedUserDetailsService(tokenAuthenticationUserDetailsService);
        return authenticationProvider;
    }

    private RefreshTokenFilter getRefreshTokenFilter() {
        var refreshTokenFilter = new RefreshTokenFilter();
        refreshTokenFilter.setAccessTokenJwsStringSerializer(accessTokenJwsStringSerializer);
        return refreshTokenFilter;
    }

    private AuthenticationFilter getJwtAuthenticationFilter(HttpSecurity builder) {
        var jwtAuthenticationFilter = new AuthenticationFilter(builder.getSharedObject(AuthenticationManager.class),
                new JwtAuthenticationConverter(accessTokenStringDeserializer, refreshTokenStringDeserializer));
        jwtAuthenticationFilter.
                setSuccessHandler(((request, response, authentication) -> {
                }));
        jwtAuthenticationFilter.
                setFailureHandler(AuthenticationGlobalExceptionHandler::authenticationFailureHandler);
        return jwtAuthenticationFilter;
    }
}
