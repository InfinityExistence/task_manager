package ru.effectivemobile.taskmanager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.effectivemobile.taskmanager.entity.dao.Comment;
import ru.effectivemobile.taskmanager.entity.dao.Task;
import ru.effectivemobile.taskmanager.entity.dao.User;
import ru.effectivemobile.taskmanager.exception.NotFoundException;
import ru.effectivemobile.taskmanager.repository.CommentRepository;
import ru.effectivemobile.taskmanager.repository.UserDataRepository;

import java.util.UUID;

import static org.springframework.data.domain.Sort.Order.by;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    CommentRepository commentRepository;
    @Autowired
    TaskService taskService;
    @Autowired
    UserDataRepository userDataRepository;

    @Override
    public Page<Comment> getCommentByTaskId(UUID idTask, int page) {
        Pageable pageable = getPageable(page);
        return commentRepository.findByTask_Id(idTask, pageable);
    }

    @Override
    public Comment addCommentToTask(Comment comment, UUID idTask) {
        comment.setId(UUID.randomUUID());
        addOwner(comment);
        Task task = taskService.getTaskById(idTask);
        comment.setTask(task);
        return commentRepository.save(comment);
    }

    private void addOwner(Comment comment) {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        User ownerData = userDataRepository.findByUserDetails_Email(name);
        comment.setOwner(ownerData);
    }

    @Override
    public Comment getCommentById(UUID idComment) {
        return commentRepository.findById(idComment).orElseThrow(() -> new NotFoundException("Комментарий не найден или не существует"));
    }

    @Override
    public void deleteComment(UUID idComment) {
        commentRepository.deleteById(idComment);
    }

    private Pageable getPageable(int page) {
        return PageRequest.
                of(page, 10,
                        Sort.by(by("time")));
    }
}
