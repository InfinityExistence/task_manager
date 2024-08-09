package ru.effectivemobile.taskmanager.security.convert;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationConverter;
import ru.effectivemobile.taskmanager.entity.request.SignInRequest;
import ru.effectivemobile.taskmanager.exception.AuthenticationException;

import java.io.IOException;

public class PasswordAuthenticationConverter implements AuthenticationConverter {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final Logger LOGGER = LoggerFactory.getLogger(PasswordAuthenticationConverter.class);

    @Override
    public Authentication convert(HttpServletRequest request) {
        try {
            SignInRequest signIn = getSignInRequest(request);
            return getUnauthenticatedUserToken(signIn);
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
            throw new AuthenticationException("Wrong request body passed");
        }
    }

    private UsernamePasswordAuthenticationToken getUnauthenticatedUserToken(SignInRequest signIn) {
        return UsernamePasswordAuthenticationToken.unauthenticated(signIn.email(), signIn.password());
    }

    private SignInRequest getSignInRequest(HttpServletRequest request) throws IOException {
        return objectMapper.readValue(request.getReader(), SignInRequest.class);
    }
}
