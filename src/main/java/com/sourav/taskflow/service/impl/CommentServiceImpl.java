package com.sourav.taskflow.service.impl;

import com.sourav.taskflow.dto.comments.CommentResponse;
import com.sourav.taskflow.dto.comments.CreateCommentRequest;
import com.sourav.taskflow.dto.comments.UpdateCommentRequest;
import com.sourav.taskflow.entity.Comment;
import com.sourav.taskflow.entity.Task;
import com.sourav.taskflow.entity.User;
import com.sourav.taskflow.enums.Role;
import com.sourav.taskflow.event.comments.CommentCreatedEvent;
import com.sourav.taskflow.event.comments.CommentDeletedEvent;
import com.sourav.taskflow.event.comments.CommentUpdatedEvent;
import com.sourav.taskflow.exception.AccessDeniedException;
import com.sourav.taskflow.exception.ResourceNotFoundException;
import com.sourav.taskflow.repository.CommentRepository;
import com.sourav.taskflow.repository.TaskRepository;
import com.sourav.taskflow.repository.UserRepository;
import com.sourav.taskflow.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public CommentResponse createComment(CreateCommentRequest commentRequest, Long taskId) {
        User user = getCurrentUser();
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new ResourceNotFoundException("Task Not Found!"));

        if (!task.getAssignee().getId().equals(user.getId()) && user.getRole() != Role.ADMIN) {
            throw new AccessDeniedException("You Are Not Allowed To Create A Comment For This Task");
        }

        Comment comment = new Comment();
        comment.setContent(commentRequest.getContent());
        comment.setTask(task);
        comment.setUser(user);

        eventPublisher.publishEvent(new CommentCreatedEvent(comment.getId(), user.getId()));

        return mapToResponse(commentRepository.save(comment));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CommentResponse> getComments(Long taskId, Pageable pageable) {
        getCurrentUser();
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new ResourceNotFoundException("Task Not Found!"));

        return commentRepository.findByTaskId(taskId, pageable).map(this::mapToResponse);
    }

    @Override
    @Transactional
    public CommentResponse updateComment(Long taskId, Long commentId, UpdateCommentRequest comment) {
        User user = getCurrentUser();
        Comment savedComment = commentRepository.findByIdAndTaskId(commentId, taskId).orElseThrow(() -> new ResourceNotFoundException("Comment Not Found For This Task!"));

        if (!savedComment.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You Are Not Allowed To Update A Comment For This Task");
        }

        if (comment.getContent() != null && !comment.getContent().isBlank()) {
            savedComment.setContent(comment.getContent());
        }

        eventPublisher.publishEvent(new CommentUpdatedEvent(savedComment.getId(), user.getId()));


        return mapToResponse(commentRepository.save(savedComment));
    }

    @Override
    @Transactional
    public void deleteComment(Long taskId, Long commentId) {
        User user = getCurrentUser();

        Comment savedComment = commentRepository.findByIdAndTaskId(commentId, taskId).orElseThrow(() -> new ResourceNotFoundException("Comment Not Found For This Task!"));

        validateCommentOwnership(savedComment, user);

        savedComment.setDeleted(true);
        savedComment.setDeletedBy(user.getId());
        savedComment.setDeletedAt(LocalDateTime.now());

        eventPublisher.publishEvent(new CommentDeletedEvent(savedComment.getId(), user.getId()));


        commentRepository.save(savedComment);
    }

    @Override
    @Transactional
    public void restoreComment(Long taskId, Long commentId) {
        User user = getCurrentUser();
        Comment comment = commentRepository.findByIdAndTaskIdIncludingDeleted(commentId, taskId).orElseThrow(() -> new ResourceNotFoundException("Comment Not Found!"));

        if (!comment.isDeleted()) {
            throw new ResourceNotFoundException("Comment Is Not Deleted!");
        }


        validateCommentOwnership(comment, user);

        comment.setDeleted(false);
        comment.setDeletedBy(null);
        comment.setDeletedAt(null);

        commentRepository.save(comment);
    }

    private void validateCommentOwnership(Comment comment, User user) {
        if (!comment.getUser().getId().equals(user.getId()) && user.getRole() != Role.ADMIN) {
            throw new AccessDeniedException("Access Denied!");
        }
    }

    private User getCurrentUser() {
        String email = Objects.requireNonNull(SecurityContextHolder
                        .getContext()
                        .getAuthentication())
                .getName();

        return userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private CommentResponse mapToResponse(Comment comment) {
        return new CommentResponse(
                comment.getId(),
                comment.getContent(),
                comment.getTask().getId(),
                comment.getTask().getTitle(),
                comment.getUser().getId(),
                comment.getUser().getName(),
                comment.getCreatedAt()
        );
    }
}
