package ru.effectivemobile.taskmanager.service;

import org.springframework.data.domain.Page;
import ru.effectivemobile.taskmanager.entity.dao.Task;

import java.util.UUID;

public interface TaskService {
    Task getTaskById(UUID id);

    Page<Task> getPageOfAllTasks(int page);

    Page<Task> getPageOfUserCreatedTasks(UUID id, int page);

    Page<Task> getPageOfUserTodoTasks(UUID id, int page);

    Task addTask(Task task);

    Task editTask(Task task);

    Task editOnlyStatusTask(Task task);

    void deleteTask(UUID task);

}
