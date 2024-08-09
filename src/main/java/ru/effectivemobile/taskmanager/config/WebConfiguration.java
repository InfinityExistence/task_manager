package ru.effectivemobile.taskmanager.config;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.crypto.DirectDecrypter;
import com.nimbusds.jose.crypto.DirectEncrypter;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jose.jwk.OctetSequenceKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import ru.effectivemobile.taskmanager.exception.AuthenticationGlobalExceptionHandler;
import ru.effectivemobile.taskmanager.security.jwt.deserialize.AccessTokenStringDeserializerImpl;
import ru.effectivemobile.taskmanager.security.jwt.deserialize.RefreshTokenStringDeserializerImpl;
import ru.effectivemobile.taskmanager.security.jwt.serialize.AccessTokenJwsStringSerializerImpl;
import ru.effectivemobile.taskmanager.security.jwt.serialize.RefreshTokenJweStringSerializerImpl;
import ru.effectivemobile.taskmanager.service.TokenAuthenticationUserDetailsService;

import java.text.ParseException;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity()
public class WebConfiguration {
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity,
                                            JwtAuthenticationConfigurer jwtAuthenticationConfigurer,
                                            PasswordAuthenticationConfigurer passwordAuthenticationConfigurer) throws Exception {

        httpSecurity.httpBasic(AbstractHttpConfigurer::disable);
        httpSecurity.formLogin(AbstractHttpConfigurer::disable);
        httpSecurity.csrf(AbstractHttpConfigurer::disable);

        httpSecurity.sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        httpSecurity.authorizeHttpRequests(request -> request
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/swagger/**").permitAll()
                .anyRequest().authenticated());

        httpSecurity.exceptionHandling(e -> e.authenticationEntryPoint(AuthenticationGlobalExceptionHandler::authenticationFailureHandler));
        httpSecurity.exceptionHandling(handle -> handle.accessDeniedHandler(
                (request, response, accessDeniedException) ->
                        response.setStatus(403)));

        httpSecurity.with(passwordAuthenticationConfigurer, Customizer.withDefaults());
        httpSecurity.with(jwtAuthenticationConfigurer, Customizer.withDefaults());

        return httpSecurity.build();
    }

    @Bean
    JwtAuthenticationConfigurer jwtAuthenticationConfigurer(
            @Value("${jwt.access-token-key}") String accessTokenKey,
            @Value("${jwt.refresh-token-key}") String refreshTokenKey,
            TokenAuthenticationUserDetailsService tokenAuthenticationUserDetailsService) throws ParseException, JOSEException {
        return JwtAuthenticationConfigurer.builder()
                .accessTokenJwsStringSerializer(new AccessTokenJwsStringSerializerImpl(
                        new MACSigner(OctetSequenceKey.parse(accessTokenKey))))
                .refreshTokenJweStringSerializer(new RefreshTokenJweStringSerializerImpl(
                        new DirectEncrypter(OctetSequenceKey.parse(refreshTokenKey))))
                .accessTokenStringDeserializer(new AccessTokenStringDeserializerImpl(
                        new MACVerifier((OctetSequenceKey.parse(accessTokenKey)))))
                .refreshTokenStringDeserializer(new RefreshTokenStringDeserializerImpl(
                        new DirectDecrypter(OctetSequenceKey.parse(refreshTokenKey))
                ))
                .tokenAuthenticationUserDetailsService(tokenAuthenticationUserDetailsService)
                .build();


    }

    @Bean
    PasswordAuthenticationConfigurer passwordAuthenticationConfigurer(UserDetailsService userDetailsService) {
        return new PasswordAuthenticationConfigurer(userDetailsService);
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
