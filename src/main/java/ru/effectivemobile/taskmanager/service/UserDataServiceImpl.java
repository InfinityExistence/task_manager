package ru.effectivemobile.taskmanager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.effectivemobile.taskmanager.entity.dao.User;
import ru.effectivemobile.taskmanager.exception.NotFoundException;
import ru.effectivemobile.taskmanager.repository.UserDataRepository;

import java.util.UUID;

@Service
public class UserDataServiceImpl implements UserDataService {
    @Autowired
    UserDataRepository userDataRepository;

    @Override
    public User getUserByEmail(String email) {
        return userDataRepository.findByUserDetails_Email(email);
    }

    @Override
    public User getUserByUuid(UUID id) {
        return userDataRepository.findById(id).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
    }

    @Override
    public Page<User> getPageOfAllUsers(int page) {
        Pageable pageable = getPageable(page);
        return userDataRepository.findAll(pageable);

    }

    @Override
    public User addUser(User user) {
        return userDataRepository.save(user);
    }

    private Pageable getPageable(int page) {
        return PageRequest.of(page, 10);
    }
}
