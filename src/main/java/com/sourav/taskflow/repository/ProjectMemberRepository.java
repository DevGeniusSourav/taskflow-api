package com.sourav.taskflow.repository;

import com.sourav.taskflow.entity.ProjectMember;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ProjectMemberRepository extends JpaRepository<ProjectMember, Long> {

    Page<ProjectMember> findByProjectId(Long projectId, Pageable pageable);

    ProjectMember findByProjectIdAndUserId(Long projectId, Long memberId);

    @Query(value = "SELECT * from project_members WHERE project_id = :projectId AND user_id = :memberId", nativeQuery = true)
    Optional<ProjectMember> findByProjectIdAndUserIdIncludingDeleted(Long projectId, Long memberId);
}
