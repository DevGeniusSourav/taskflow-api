package com.sourav.taskflow.repository;

import com.sourav.taskflow.entity.Comment;
import com.sourav.taskflow.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Optional<Comment> findById(Long id);

    Page<Comment> findByTaskId(Long taskId, Pageable pageable);

    @Query(value = "SELECT * FROM comments WHERE id = :id AND task_id = :taskId", nativeQuery = true)
    Optional<Comment> findByIdAndTaskIdIncludingDeleted(Long id, Long taskId);

    Optional<Comment> findByIdAndTaskId(Long id, Long taskId);

    Long task(Task task);
}
