package ru.effectivemobile.taskmanager.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.effectivemobile.taskmanager.entity.dao.Task;

import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {
    Page<Task> findByActor_Id(UUID id, Pageable pageable);

    Page<Task> findByOwner_Id(UUID id, Pageable pageable);
}
