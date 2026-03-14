package com.sourav.taskflow.repository;

import com.sourav.taskflow.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
