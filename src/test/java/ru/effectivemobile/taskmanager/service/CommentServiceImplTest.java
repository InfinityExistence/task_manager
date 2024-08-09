package ru.effectivemobile.taskmanager.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.security.core.Authentication;
import ru.effectivemobile.taskmanager.entity.dao.Comment;
import ru.effectivemobile.taskmanager.entity.dao.Task;
import ru.effectivemobile.taskmanager.exception.NotFoundException;
import ru.effectivemobile.taskmanager.repository.CommentRepository;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private TaskService taskService;

    @InjectMocks
    private CommentServiceImpl commentService;


    @Test
    @DisplayName("Получение комментария по Id задания.")
    void testGetCommentByTaskId_Success() {
        UUID taskId = UUID.randomUUID();
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Order.by("time")));
        Page<Comment> comments = new PageImpl<>(Collections.emptyList());

        when(commentRepository.findByTask_Id(taskId, pageable)).thenReturn(comments);

        Page<Comment> result = commentService.getCommentByTaskId(taskId, 0);

        assertEquals(comments, result);
        verify(commentRepository).findByTask_Id(taskId, pageable);
    }



    @Test
    @DisplayName("Получение конкретного комментария по Id. Успех.")
    void testGetCommentById_Success() {
        UUID commentId = UUID.randomUUID();
        Comment comment = new Comment();
        comment.setId(commentId);

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

        Comment result = commentService.getCommentById(commentId);

        assertEquals(comment, result);
        verify(commentRepository).findById(commentId);
    }

    @Test
    @DisplayName("Получение комментария по Id(не найден). Выброс исключения.")
    void testGetCommentById_NotFound_ThrowsNotFoundException() {
        UUID commentId = UUID.randomUUID();

        when(commentRepository.findById(commentId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () ->
                commentService.getCommentById(commentId));
    }

    @Test
    @DisplayName("Удаление комментария. Успешно")
    void testDeleteComment_Success() {
        UUID commentId = UUID.randomUUID();

        commentService.deleteComment(commentId);

        verify(commentRepository).deleteById(commentId);
    }


}