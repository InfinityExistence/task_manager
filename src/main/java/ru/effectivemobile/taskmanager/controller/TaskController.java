package ru.effectivemobile.taskmanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.effectivemobile.taskmanager.entity.dao.Task;
import ru.effectivemobile.taskmanager.security.TaskOwnerCheck;
import ru.effectivemobile.taskmanager.service.TaskService;
import ru.effectivemobile.taskmanager.service.UserDataService;

import java.util.UUID;

@RestController
@RequestMapping("/api/task")
public class TaskController {
    @Autowired
    TaskOwnerCheck taskOwnerCheck;
    @Autowired
    TaskService taskService;
    @Autowired
    UserDataService userDataService;

    @GetMapping("/all")
    Page<Task> getAllTasks(
            @RequestParam(defaultValue = "0") int page) {

        return taskService.getPageOfAllTasks(page);
    }

    @GetMapping("/owner")
    Page<Task> getCreatedTasks(@RequestParam(required = false) UUID userId,
                               @RequestParam(defaultValue = "0") int page, Authentication authentication) {

        if (userId == null) {
            userId = userDataService.getUserByEmail(authentication.getName())
                    .getId();
        }
        return taskService.getPageOfUserCreatedTasks(userId, page);
    }

    @GetMapping("/todo")
    Page<Task> getTodoTasks(@RequestParam(required = false) UUID userId,
                            @RequestParam(defaultValue = "0") int page, Authentication authentication) {
        if (userId == null) {
            userId = userDataService.getUserByEmail(authentication.getName())
                    .getId();
        }
        return taskService.getPageOfUserTodoTasks(userId, page);
    }

    @PutMapping("/add")
    Task createTask(@RequestBody Task task) {
        return taskService.addTask(task);
    }

    @PatchMapping("/edit")
    @PreAuthorize("@taskOwnerCheck.checkIsOwner(#task, authentication)")
    Task editTask(@RequestBody Task task) {
        return taskService.editTask(task);
    }

    @PatchMapping("/edit/status")
    @PreAuthorize("@taskOwnerCheck.checkIsOwnerOrActor(#task, authentication)")
    Task editStatusTask(@RequestBody Task task) {
        return taskService.editOnlyStatusTask(task);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("@taskOwnerCheck.checkIsOwner(#taskId, authentication)")
    void deleteTask(@RequestParam UUID taskId) {
        taskService.deleteTask(taskId);
    }

}
