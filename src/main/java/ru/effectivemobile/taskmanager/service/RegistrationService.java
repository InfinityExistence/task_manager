package ru.effectivemobile.taskmanager.service;

import ru.effectivemobile.taskmanager.entity.dao.PasswordUserDetails;

public interface RegistrationService {
    void register(PasswordUserDetails userDetails, String name);
}
