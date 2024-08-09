package ru.effectivemobile.taskmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.effectivemobile.taskmanager.entity.dao.DeactivatedToken;

import java.util.UUID;

public interface DeactivatedTokenRepository extends JpaRepository<DeactivatedToken, UUID> {
}
