package com.sourav.taskflow.controller;

import com.sourav.taskflow.dto.ApiResponse;
import com.sourav.taskflow.dto.comments.CommentResponse;
import com.sourav.taskflow.dto.comments.CreateCommentRequest;
import com.sourav.taskflow.dto.comments.UpdateCommentRequest;
import com.sourav.taskflow.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks/{taskId}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    @Operation(summary = "Create a new comment")
    public ResponseEntity<ApiResponse<CommentResponse>> createComment(@Valid @RequestBody CreateCommentRequest comment, @PathVariable("taskId") Long taskId) {
        return ResponseEntity.ok(new ApiResponse<>(commentService.createComment(comment, taskId), "Comment Created Successfully"));
    }

    @GetMapping
    @Operation(summary = "Get comments for a particular task")
    public ResponseEntity<ApiResponse<Page<CommentResponse>>> getCommentsForTask(@PathVariable("taskId") Long taskId, Pageable pageable) {
        return ResponseEntity.ok(new ApiResponse<>(commentService.getComments(taskId, pageable), "Comments Fetched Successfully"));
    }

    @PatchMapping("/{commentId}")
    @Operation(summary = "Update comment for a particular task")
    public ResponseEntity<ApiResponse<CommentResponse>> updateComment(@PathVariable("taskId") Long taskId, @PathVariable("commentId") Long commentId, @Valid @RequestBody UpdateCommentRequest commentRequest) {
        return ResponseEntity.ok(new ApiResponse<>(commentService.updateComment(taskId, commentId, commentRequest), "Comment Updated Successfully"));
    }

    @DeleteMapping("/{commentId}")
    @Operation(summary = "Delete comment for a particular task")
    public ResponseEntity<ApiResponse<String>> deleteComment(@PathVariable("taskId") Long taskId, @PathVariable("commentId") Long commentId) {
        commentService.deleteComment(taskId, commentId);
        return ResponseEntity.ok(new ApiResponse<>("Comment Deleted Successfully!"));
    }

    @PutMapping("/restore/{id}")
    @Operation(summary = "Restore comment for a particular task")
    public ResponseEntity<ApiResponse<String>> restoreComment(@PathVariable("taskId") Long taskId, @PathVariable("id") Long commentId) {
        commentService.restoreComment(taskId, commentId);
        return ResponseEntity.ok(new ApiResponse<>("Comment Restored Successfully!"));
    }

}
