package ru.effectivemobile.taskmanager.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.effectivemobile.taskmanager.entity.dao.PasswordUserDetails;
import ru.effectivemobile.taskmanager.entity.dao.User;
import ru.effectivemobile.taskmanager.exception.AuthenticationException;
import ru.effectivemobile.taskmanager.repository.UserDetailsRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegistrationServiceImplTest {
    @InjectMocks
    RegistrationServiceImpl registrationService;
    @Mock
    UserDetailsRepository userDetailsRepository;
    @Mock
    UserDataService userDataService;
    @Mock
    PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("Успешная регистрация пользователя")
    void testRegister_ValidDetails_Success() {
        PasswordUserDetails details = new PasswordUserDetails();
        details.setEmail("test@example.com");
        details.setPassword("validPassword123");

        when(userDetailsRepository.existsById(details.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(details.getPassword())).thenReturn("encodedPassword");
        when(userDetailsRepository.save(details)).thenReturn(details);

        assertDoesNotThrow(() -> registrationService.register(details, "Test User"));

        verify(userDetailsRepository).save(details);
        verify(userDataService).addUser(any(User.class));
    }

    @Test
    @DisplayName("Регистрация с невалидным email")
    void testRegister_InvalidEmail_ThrowsAuthenticationException() {
        PasswordUserDetails details = new PasswordUserDetails();
        details.setEmail("invalid-email");
        details.setPassword("validPassword123");

        assertThrows(AuthenticationException.class, () ->
                registrationService.register(details, "Test User"));

    }

    @Test
    @DisplayName("Регистрация с невалидным паролем")
    void testRegister_InvalidPassword_ThrowsAuthenticationException() {
        PasswordUserDetails details = new PasswordUserDetails();
        details.setEmail("test@example.com");
        details.setPassword("short");

        assertThrows(AuthenticationException.class, () ->
                registrationService.register(details, "Test User"));
    }

    @Test
    @DisplayName("Регистрация существующего пользователя")
    void testRegister_UserAlreadyExists_ThrowsAuthenticationException() {
        PasswordUserDetails details = new PasswordUserDetails();
        details.setEmail("test@example.com");
        details.setPassword("validPassword123");

        when(userDetailsRepository.existsById(details.getEmail())).thenReturn(true);

        AuthenticationException exception = assertThrows(AuthenticationException.class, () ->
                registrationService.register(details, "Test User"));

        assertEquals("Данный пользователь уже существует", exception.getMessage());
    }

}