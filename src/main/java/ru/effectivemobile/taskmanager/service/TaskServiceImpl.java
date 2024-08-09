package ru.effectivemobile.taskmanager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.effectivemobile.taskmanager.entity.dao.Task;
import ru.effectivemobile.taskmanager.entity.dao.User;
import ru.effectivemobile.taskmanager.exception.NotFoundException;
import ru.effectivemobile.taskmanager.repository.TaskRepository;
import ru.effectivemobile.taskmanager.repository.UserDataRepository;

import java.util.Optional;
import java.util.UUID;

import static org.springframework.data.domain.Sort.Order.by;

@Service
public class TaskServiceImpl implements TaskService {
    @Autowired
    TaskRepository taskRepository;
    @Autowired
    UserDataRepository userDataRepository;

    @Override
    public Task getTaskById(UUID id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Задача не существует"));
    }

    @Override
    public Page<Task> getPageOfAllTasks(int page) {
        Pageable pageable = getPageable(page);
        return taskRepository.findAll(pageable);
    }

    @Override
    public Page<Task> getPageOfUserCreatedTasks(UUID id, int page) {
        Pageable pageable = getPageable(page);
        return taskRepository.findByOwner_Id(id, pageable);
    }

    @Override
    public Page<Task> getPageOfUserTodoTasks(UUID id, int page) {
        Pageable pageable = getPageable(page);
        return taskRepository.findByActor_Id(id, pageable);
    }

    @Override
    public Task addTask(Task task) {
        addOwner(task);
        addActorIfPresent(task);

        task.setId(UUID.randomUUID());
        return taskRepository.save(task);
    }

    private void addActorIfPresent(Task task) {
        if (task.getActor() != null) {
            UUID id = task.getActor().getId();
            Optional<User> actor = userDataRepository.findById(id);
            actor.ifPresent(task::setActor);
        }
    }

    private void addOwner(Task task) {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        User ownerData = userDataRepository.findByUserDetails_Email(name);
        task.setOwner(ownerData);
    }

    @Override
    public Task editTask(Task task) {
        Task originalTask = taskRepository.findById(task.getId())
                .orElseThrow(() -> new NotFoundException("Задача не существует"));
        task.setOwner(task.getOwner());
        addActorIfPresent(task);
        setOldPropertiesIfNull(task, originalTask);
        return taskRepository.save(task);
    }

    private static void setOldPropertiesIfNull(Task task, Task originalTask) {
        if (task.getActor() == null)
            task.setActor(originalTask.getActor());
        if (task.getDescription() == null)
            task.setDescription(originalTask.getDescription());
        if (task.getTitle() == null)
            task.setTitle(originalTask.getTitle());
        if (task.getTitle() == null)
            task.setTitle(originalTask.getTitle());
        if (task.getStatus() == null)
            task.setStatus(originalTask.getStatus());
        if (task.getPriority() == null)
            task.setPriority(originalTask.getPriority());
    }

    @Override
    public Task editOnlyStatusTask(Task task) {
        var taskDb = taskRepository.findById(task.getId())
                .orElseThrow(() -> new NotFoundException("Такого задания не существует"));
        taskDb.setStatus(task.getStatus());
        return taskRepository.save(taskDb);
    }

    @Override
    public void deleteTask(UUID task) {
        taskRepository.deleteById(task);
    }

    private Pageable getPageable(int page) {
        return PageRequest.
                of(page, 10,
                        Sort.by(by("status"), by("priority")));
    }
}
