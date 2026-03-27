package com.sourav.taskflow.repository;

import com.sourav.taskflow.entity.Task;
import com.sourav.taskflow.entity.User;
import com.sourav.taskflow.enums.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    Page<Task> findByProjectIdAndAssigneeId(Long projectId, Long assigneeId, Pageable pageable);

    Page<Task> findByStatusAndAssigneeId(TaskStatus status, Long assigneeId, Pageable pageable);

    Page<Task> findByProjectIdAndStatusAndAssigneeId(Long projectId, TaskStatus status, Long assigneeId, Pageable pageable);

    Page<Task> findByAssigneeId(Long assigneeId, Pageable pageable);

    @Query(value = "SELECT * FROM tasks WHERE id = :id", nativeQuery = true)
    Optional<Task> findByIdIncludingDeleted(@Param("id") Long id);

    Optional<List<Task>> findByProjectId(Long projectId);
}
