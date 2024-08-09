package ru.effectivemobile.taskmanager.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import ru.effectivemobile.taskmanager.entity.dao.Comment;
import ru.effectivemobile.taskmanager.service.CommentService;

@Component
public class CommentOwnerCheck {
    @Autowired
    CommentService commentService;

    public boolean checkIsOwner(Comment comment, Authentication authentication) {
        var dbComment = commentService.getCommentById(comment.getId());
        return isOwner(dbComment, authentication);
    }

    private boolean isOwner(Comment comment, Authentication authentication) {
        return comment.getOwner().getUserDetails().getUsername().equals(authentication.getName());
    }
}
