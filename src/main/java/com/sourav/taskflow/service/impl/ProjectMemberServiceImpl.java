package com.sourav.taskflow.service.impl;

import com.sourav.taskflow.dto.projectMembers.ProjectMemberResponse;
import com.sourav.taskflow.entity.Project;
import com.sourav.taskflow.entity.ProjectMember;
import com.sourav.taskflow.entity.User;
import com.sourav.taskflow.enums.Role;
import com.sourav.taskflow.exception.AccessDeniedException;
import com.sourav.taskflow.exception.GeneralException;
import com.sourav.taskflow.exception.ResourceNotFoundException;
import com.sourav.taskflow.repository.ProjectMemberRepository;
import com.sourav.taskflow.repository.ProjectRepository;
import com.sourav.taskflow.repository.UserRepository;
import com.sourav.taskflow.service.ProjectMemberService;
import lombok.RequiredArgsConstructor;
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
public class ProjectMemberServiceImpl implements ProjectMemberService {

    private final ProjectMemberRepository projectMemberRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    @Override
    public void addMember(Long projectId, Long memberId) {
        User user = getCurrentUser();

        Project project = projectRepository.findById(projectId).orElseThrow(() -> new ResourceNotFoundException("Project Not Found"));

        validateProjectOwnership(project, user);

        User member = userRepository.findById(memberId).orElseThrow(() -> new ResourceNotFoundException("User Not Found"));

        ProjectMember existing = projectMemberRepository.findByProjectIdAndUserIdIncludingDeleted(projectId, memberId).orElse(null);

        if (existing != null) {
            if(!existing.isDeleted()){
                throw new GeneralException("Project Member Already Exists In This Project");
            }
            existing.setDeleted(false);
            existing.setDeletedBy(null);
            existing.setDeletedAt(null);

            projectMemberRepository.save(existing);
            return;
        }

        ProjectMember projectMember = new ProjectMember();

        projectMember.setProject(project);
        projectMember.setUser(member);

        projectMemberRepository.save(projectMember);
    }

    @Override
    public Page<ProjectMemberResponse> getProjectMembers(Long projectId, Pageable pageable) {
        getCurrentUser();

        Project project = projectRepository.findById(projectId).orElseThrow(() -> new GeneralException("Project Not Found"));
        return projectMemberRepository.findByProjectId(project.getId(), pageable).map(this::mapToResponse);
    }


    @Override
    public void removeMember(Long projectId, Long memberId) {
        User user = getCurrentUser();

        Project project = projectRepository.findById(projectId).orElseThrow(() -> new ResourceNotFoundException("Project Not Found"));

        validateProjectOwnership(project, user);

        User member = userRepository.findById(memberId).orElseThrow(() -> new ResourceNotFoundException("User Not Found"));

        if (project.getOwner().getId().equals(member.getId())) {
            throw new GeneralException("Cannot Remove Owner Member");
        }

        ProjectMember projectMember = projectMemberRepository.findByProjectIdAndUserId(projectId, memberId);

        if (Objects.isNull(projectMember)) {
            throw new ResourceNotFoundException("Project Member Does Not Exist In This Project");
        }

        projectMember.setDeleted(true);
        projectMember.setDeletedBy(user.getId());
        projectMember.setDeletedAt(LocalDateTime.now());

        projectMemberRepository.save(projectMember);
    }

    @Override
    public void restoreMember(Long projectId, Long memberId) {
        User user = getCurrentUser();

        Project project = projectRepository.findById(projectId).orElseThrow(() -> new ResourceNotFoundException("Project Not Found"));

        validateProjectOwnership(project, user);

        User member = userRepository.findById(memberId).orElseThrow(() -> new ResourceNotFoundException("User Not Found"));

        ProjectMember projectMember = projectMemberRepository.findByProjectIdAndUserIdIncludingDeleted(projectId, memberId).orElseThrow(() -> new ResourceNotFoundException("Project Member Never Existed In This Project"));

        if (!projectMember.isDeleted()) {
            throw new GeneralException("Project Member Is Not Deleted!");
        }

        projectMember.setDeleted(false);
        projectMember.setDeletedBy(null);
        projectMember.setDeletedAt(null);

        projectMemberRepository.save(projectMember);
    }

    private ProjectMemberResponse mapToResponse(ProjectMember projectMember) {
        return new ProjectMemberResponse(
                projectMember.getId(),
                projectMember.getProject().getId(),
                projectMember.getProject().getName(),
                projectMember.getUser().getId(),
                projectMember.getUser().getName()
        );
    }

    public void validateProjectOwnership(Project project, User user) {
        if (!project.getOwner().getId().equals(user.getId()) && user.getRole() != Role.ADMIN) {
            throw new AccessDeniedException("Access Denied");
        }
    }

    private User getCurrentUser() {
        String email = Objects.requireNonNull(SecurityContextHolder
                        .getContext()
                        .getAuthentication())
                .getName();

        return userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}
