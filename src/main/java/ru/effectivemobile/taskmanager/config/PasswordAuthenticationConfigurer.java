package ru.effectivemobile.taskmanager.config;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import ru.effectivemobile.taskmanager.exception.AuthenticationGlobalExceptionHandler;
import ru.effectivemobile.taskmanager.security.convert.PasswordAuthenticationConverter;
import ru.effectivemobile.taskmanager.security.filter.LoginBodyRequestFilter;

@RequiredArgsConstructor
@AllArgsConstructor
public class PasswordAuthenticationConfigurer extends AbstractHttpConfigurer<PasswordAuthenticationConfigurer, HttpSecurity> {
    private final UserDetailsService userDetailsService;
    private RequestMatcher requestMatcher = new AntPathRequestMatcher("/api/auth/login", HttpMethod.POST.name());

    @Override
    public void configure(HttpSecurity builder) {
        AuthenticationManager manager = builder.getSharedObject(AuthenticationManager.class);

        var passwordAuthenticationFilter = getPasswordAuthenticationFilter(manager);

        AuthenticationProvider authenticationProvider = authenticationProvider();

        builder.addFilterBefore(passwordAuthenticationFilter, LogoutFilter.class)
                .authenticationProvider(authenticationProvider);
    }

    private LoginBodyRequestFilter getPasswordAuthenticationFilter(AuthenticationManager manager) {
        var passwordAuthenticationFilter = new LoginBodyRequestFilter(manager,
                new PasswordAuthenticationConverter());

        passwordAuthenticationFilter.setRequestMatcher(requestMatcher);
        passwordAuthenticationFilter.setSuccessHandler(((request, response, authentication) -> {
        }));
        passwordAuthenticationFilter.
                setFailureHandler(AuthenticationGlobalExceptionHandler::authenticationFailureHandler);
        return passwordAuthenticationFilter;
    }

    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider(new BCryptPasswordEncoder());
        authenticationProvider.setUserDetailsService(userDetailsService);
        return authenticationProvider;
    }
}
