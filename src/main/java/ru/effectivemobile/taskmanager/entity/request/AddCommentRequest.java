package ru.effectivemobile.taskmanager.entity.request;

import ru.effectivemobile.taskmanager.entity.dao.Comment;

import java.util.UUID;

public record AddCommentRequest(UUID taskId, Comment comment) {
}
