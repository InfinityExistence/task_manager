package ru.effectivemobile.taskmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.effectivemobile.taskmanager.entity.dao.User;

import java.util.UUID;

public interface UserDataRepository extends JpaRepository<User, UUID> {
    User findByUserDetails_Email(String email);
}
