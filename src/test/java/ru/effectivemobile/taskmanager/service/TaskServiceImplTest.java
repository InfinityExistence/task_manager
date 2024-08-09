package ru.effectivemobile.taskmanager.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.effectivemobile.taskmanager.entity.Status;
import ru.effectivemobile.taskmanager.entity.dao.Task;
import ru.effectivemobile.taskmanager.entity.dao.User;
import ru.effectivemobile.taskmanager.repository.TaskRepository;
import ru.effectivemobile.taskmanager.repository.UserDataRepository;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserDataRepository userDataRepository;

    @InjectMocks
    private TaskServiceImpl taskService;


    @Test
    @DisplayName("Получение задачи по Id")
    void testGetTaskById_Success() {
        UUID taskId = UUID.randomUUID();
        Task task = new Task();
        task.setId(taskId);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

        Task result = taskService.getTaskById(taskId);

        assertEquals(task, result);
        verify(taskRepository).findById(taskId);
    }

    @Test
    @DisplayName("Получение страницы всех задач")
    void testGetPageOfAllTasks_Success() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Order.by("status"), Sort.Order.by("priority")));
        Page<Task> tasks = new PageImpl<>(Collections.emptyList());

        when(taskRepository.findAll(pageable)).thenReturn(tasks);

        Page<Task> result = taskService.getPageOfAllTasks(0);

        assertEquals(tasks, result);
        verify(taskRepository).findAll(pageable);
    }

    @Test
    @DisplayName("Получение страницы задач, созданных пользователем")
    void testGetPageOfUserCreatedTasks_Success() {
        UUID userId = UUID.randomUUID();
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Order.by("status"), Sort.Order.by("priority")));
        Page<Task> tasks = new PageImpl<>(Collections.emptyList());

        when(taskRepository.findByOwner_Id(userId, pageable)).thenReturn(tasks);

        Page<Task> result = taskService.getPageOfUserCreatedTasks(userId, 0);

        assertEquals(tasks, result);
        verify(taskRepository).findByOwner_Id(userId, pageable);
    }

    @Test
    @DisplayName("Получение страницы задач, назначенных пользователю")
    void testGetPageOfUserTodoTasks_Success() {
        UUID userId = UUID.randomUUID();
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Order.by("status"), Sort.Order.by("priority")));
        Page<Task> tasks = new PageImpl<>(Collections.emptyList());

        when(taskRepository.findByActor_Id(userId, pageable)).thenReturn(tasks);

        Page<Task> result = taskService.getPageOfUserTodoTasks(userId, 0);

        assertEquals(tasks, result);
        verify(taskRepository).findByActor_Id(userId, pageable);
    }

    @Test
    @DisplayName("Добавление новой задачи")
    void testAddTask_Success() {
        Task task = new Task();
        User owner = new User();
        owner.setId(UUID.randomUUID());

        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn("test@example.com");

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(userDataRepository.findByUserDetails_Email("test@example.com")).thenReturn(owner);
        when(taskRepository.save(task)).thenReturn(task);

        Task result = taskService.addTask(task);

        assertNotNull(result.getId());
        assertEquals(owner, result.getOwner());
        verify(taskRepository).save(task);
    }

    @Test
    @DisplayName("Редактирование задачи")
    void testEditTask_Success() {
        Task taskDb = new Task();
        taskDb.setId(UUID.randomUUID());
        taskDb.setOwner(new User());
        Task task = new Task();
        task.setId(taskDb.getId());


        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(taskDb));


        when(taskRepository.save(task)).thenReturn(task);

        Task result = taskService.editTask(task);

        assertEquals(task, result);
        verify(taskRepository).save(task);
    }

    @Test
    @DisplayName("Редактирование только статуса задачи")
    void testEditOnlyStatusTask_Success() {
        Task task = new Task();
        task.setId(UUID.randomUUID());
        task.setStatus(Status.WAITING);

        Task taskDb = new Task();
        taskDb.setId(task.getId());

        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(taskDb));
        when(taskRepository.save(taskDb)).thenReturn(taskDb);

        Task result = taskService.editOnlyStatusTask(task);

        assertEquals(Status.WAITING, result.getStatus());
        verify(taskRepository).save(taskDb);
    }

    @Test
    @DisplayName("Удаление задачи")
    void testDeleteTask_Success() {
        UUID taskId = UUID.randomUUID();

        taskService.deleteTask(taskId);

        verify(taskRepository).deleteById(taskId);
    }
}