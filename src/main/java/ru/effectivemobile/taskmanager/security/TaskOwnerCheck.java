package ru.effectivemobile.taskmanager.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import ru.effectivemobile.taskmanager.entity.dao.Task;
import ru.effectivemobile.taskmanager.service.TaskService;

import java.util.UUID;

@Component
public class TaskOwnerCheck {
    @Autowired
    TaskService taskService;

    public boolean checkIsOwner(Task task, Authentication authentication) {
        var dbTask = taskService.getTaskById(task.getId());
        return isOwner(dbTask, authentication);
    }

    public boolean checkIsOwner(UUID taskId, Authentication authentication) {
        var dbTask = taskService.getTaskById(taskId);
        return isOwner(dbTask, authentication);
    }

    public boolean checkIsOwnerOrActor(Task task, Authentication authentication) {
        var dbTask = taskService.getTaskById(task.getId());
        return isOwner(dbTask, authentication) ||
                isActor(dbTask, authentication);
    }

    private boolean isOwner(Task dbTask, Authentication authentication) {
        return dbTask.getOwner().getUserDetails().getUsername().equals(authentication.getName());
    }

    private static boolean isActor(Task dbTask, Authentication authentication) {
        return dbTask.getActor().getUserDetails().getUsername().equals(authentication.getName());
    }
}
