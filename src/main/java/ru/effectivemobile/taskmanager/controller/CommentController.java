package ru.effectivemobile.taskmanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.effectivemobile.taskmanager.entity.dao.Comment;
import ru.effectivemobile.taskmanager.entity.request.AddCommentRequest;
import ru.effectivemobile.taskmanager.security.CommentOwnerCheck;
import ru.effectivemobile.taskmanager.service.CommentService;

import java.util.UUID;

@RestController
@RequestMapping("/api/comment")
public class CommentController {
    @Autowired
    CommentOwnerCheck commentOwnerCheck;
    @Autowired
    CommentService commentService;

    @GetMapping("all")
    Page<Comment> getCommentsByTask(@RequestParam UUID taskId,
                                    @RequestParam(defaultValue = "0", required = false) int page) {
        return commentService.getCommentByTaskId(taskId, page);
    }

    @GetMapping
    Comment getCommentById(@RequestParam UUID commentId) {
        return commentService.getCommentById(commentId);
    }

    @PostMapping
    Comment addComment(@RequestBody AddCommentRequest addCommentRequest) {
        return commentService.addCommentToTask(addCommentRequest.comment(), addCommentRequest.taskId());
    }

    @DeleteMapping
    @PreAuthorize("@commentOwnerCheck.checkIsOwner(#comment, authentication)")
    void deleteComment(@RequestParam UUID commentId) {
        commentService.deleteComment(commentId);
    }


}
