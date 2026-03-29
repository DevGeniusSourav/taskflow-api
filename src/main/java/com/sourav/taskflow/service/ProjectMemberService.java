package com.sourav.taskflow.service;

import com.sourav.taskflow.dto.projectMembers.ProjectMemberResponse;
import com.sourav.taskflow.entity.ProjectMember;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProjectMemberService {

    void addMember(Long projectId, Long memberId);

    Page<ProjectMemberResponse> getProjectMembers(Long projectId, Pageable pageable);

    void removeMember(Long projectId, Long memberId);
    
    void restoreMember(Long projectId, Long memberId);
}
