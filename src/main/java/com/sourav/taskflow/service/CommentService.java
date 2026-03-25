package com.sourav.taskflow.service;

import com.sourav.taskflow.dto.comments.CommentResponse;
import com.sourav.taskflow.dto.comments.CreateCommentRequest;
import com.sourav.taskflow.dto.comments.UpdateCommentRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentService {

    CommentResponse createComment(CreateCommentRequest comment, Long taskId);

    Page<CommentResponse> getComments(Long taskId, Pageable pageable);

    CommentResponse updateComment(Long taskId, Long commentId, UpdateCommentRequest comment);

    void deleteComment(Long taskId, Long commentId);

    void restoreComment(Long taskId, Long commentId);
}
