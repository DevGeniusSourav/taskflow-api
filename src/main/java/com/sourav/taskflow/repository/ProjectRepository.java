package com.sourav.taskflow.repository;

import com.sourav.taskflow.entity.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    Page<Project> findByOwnerId(Long id, Pageable pageable);

    @Query(value = "SELECT * FROM projects WHERE id = :id", nativeQuery = true)
    Optional<Project> findByIdIncludingDeleted(Long id);
}
