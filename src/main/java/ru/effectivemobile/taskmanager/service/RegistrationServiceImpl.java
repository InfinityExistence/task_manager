package ru.effectivemobile.taskmanager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.effectivemobile.taskmanager.entity.dao.PasswordUserDetails;
import ru.effectivemobile.taskmanager.entity.dao.User;
import ru.effectivemobile.taskmanager.exception.AuthenticationException;
import ru.effectivemobile.taskmanager.repository.UserDetailsRepository;

import java.util.UUID;
import java.util.regex.Pattern;

@Service
public class RegistrationServiceImpl implements RegistrationService {
    @Autowired
    UserDetailsRepository userDetailsRepository;
    @Autowired
    UserDataService userDataService;
    @Autowired
    PasswordEncoder passwordEncoder;
    private final Pattern emailPattern = Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");

    @Override
    public void register(PasswordUserDetails details, String name) {
        checkIsUserExistOrThrow(details);
        if (isValidEmail(details.getEmail()) &&
                isValidPassword(details.getPassword()) &&
                name != null) {
            details.setPassword(passwordEncoder.encode(details.getPassword()));
            PasswordUserDetails saved = userDetailsRepository.save(details);

            addUserData(name, saved);
        } else throw new AuthenticationException("""
                Введен не валидный email или пароль.
                (Пароль должен быть длиннее 8 символов)
                Так же необзодимо ввести имя пользователя""");
    }

    private void checkIsUserExistOrThrow(PasswordUserDetails details) {
        if (userDetailsRepository.existsById(details.getEmail()))
            throw new AuthenticationException("Данный пользователь уже существует");
    }

    private void addUserData(String name, PasswordUserDetails saved) {
        User user = User.builder()
                .id(UUID.randomUUID())
                .name(name)
                .userDetails(saved).build();
        userDataService.addUser(user);
    }

    private boolean isValidEmail(String email) {
        return emailPattern.matcher(email).find();
    }

    private boolean isValidPassword(String password) {
        int length = password.length();
        return length > 8 && length < 128;
    }

}
