package com.sourav.taskflow.controller;

import com.sourav.taskflow.dto.comments.CommentResponse;
import com.sourav.taskflow.dto.comments.CreateCommentRequest;
import com.sourav.taskflow.dto.comments.UpdateCommentRequest;
import com.sourav.taskflow.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks/{taskId}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    @Operation(summary = "Create a new comment")
    public ResponseEntity<CommentResponse> createComment(@Valid @RequestBody CreateCommentRequest comment, @PathVariable("taskId") Long taskId) {
        return ResponseEntity.ok().body(commentService.createComment(comment, taskId));
    }

    @GetMapping
    @Operation(summary = "Get comments for a particular task")
    public ResponseEntity<Page<CommentResponse>> getCommentsForTask(@PathVariable("taskId") Long taskId, Pageable pageable) {
        return ResponseEntity.ok().body(commentService.getComments(taskId, pageable));
    }

    @PatchMapping("/{commentId}")
    @Operation(summary = "Update comment for a particular task")
    public ResponseEntity<CommentResponse> updateComment(@PathVariable("taskId") Long taskId, @PathVariable("commentId") Long commentId, @Valid @RequestBody UpdateCommentRequest commentRequest) {
        return ResponseEntity.ok().body(commentService.updateComment(taskId, commentId, commentRequest));
    }

    @DeleteMapping("/{commentId}")
    @Operation(summary = "Delete comment for a particular task")
    public ResponseEntity<?> deleteComment(@PathVariable("taskId") Long taskId, @PathVariable("commentId") Long commentId) {
        commentService.deleteComment(taskId, commentId);
        return ResponseEntity.status(HttpStatus.OK).body("Comment Deleted Successfully!");
    }

    @PutMapping("/restore/{id}")
    @Operation(summary = "Restore comment for a particular task")
    public ResponseEntity<?> restoreComment(@PathVariable("taskId") Long taskId, @PathVariable("id") Long commentId) {
        commentService.restoreComment(taskId, commentId);
        return ResponseEntity.status(HttpStatus.OK).body("Comment Restored Successfully!");
    }

}
