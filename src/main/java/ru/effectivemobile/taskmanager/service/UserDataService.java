package ru.effectivemobile.taskmanager.service;

import org.springframework.data.domain.Page;
import ru.effectivemobile.taskmanager.entity.dao.User;

import java.util.UUID;

public interface UserDataService {
    User getUserByEmail(String email);

    User getUserByUuid(UUID id);

    Page<User> getPageOfAllUsers(int page);

    User addUser(User user);
}
