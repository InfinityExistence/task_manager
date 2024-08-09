package ru.effectivemobile.taskmanager.service;

import org.springframework.data.domain.Page;
import ru.effectivemobile.taskmanager.entity.dao.Comment;

import java.util.UUID;

public interface CommentService {
    Page<Comment> getCommentByTaskId(UUID idTask, int page);

    Comment addCommentToTask(Comment comment, UUID idTask);

    Comment getCommentById(UUID idComment);

    void deleteComment(UUID idComment);

}
