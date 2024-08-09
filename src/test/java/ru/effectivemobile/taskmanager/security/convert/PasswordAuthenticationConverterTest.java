package ru.effectivemobile.taskmanager.security.convert;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class PasswordAuthenticationConverterTest {
    @Mock
    HttpServletRequest request;
    final PasswordAuthenticationConverter passwordAuthenticationConverter = new PasswordAuthenticationConverter();

    @Test
    @DisplayName("Запрос на авторизацию через пароль. Валидный")
    void convertComplete() throws IOException {
        String requestText = """
                {"email":"email","password":"password"}""";
        Authentication expected = UsernamePasswordAuthenticationToken.unauthenticated("email", "password");

        doReturn(new BufferedReader(new StringReader(requestText))).when(request).getReader();

        var result = passwordAuthenticationConverter.convert(request);

        assertEquals(expected, result);

    }

    @Test
    @DisplayName("Не правильный запрос на авторизацию через пароль. Выброс исключения.")
    void convertThrowsException() throws IOException {

        String requestText = """
                {"emai":"email","password":"password"}""";
        Authentication expected = UsernamePasswordAuthenticationToken.unauthenticated("email", "password");

        doReturn(new BufferedReader(new StringReader(requestText))).when(request).getReader();


        assertThrows(AuthenticationException.class, () -> passwordAuthenticationConverter.convert(request));
    }

}