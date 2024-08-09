package ru.effectivemobile.taskmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.effectivemobile.taskmanager.entity.dao.PasswordUserDetails;

public interface UserDetailsRepository extends JpaRepository<PasswordUserDetails, String> {
}
