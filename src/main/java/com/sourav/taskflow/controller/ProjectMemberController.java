package com.sourav.taskflow.controller;

import com.sourav.taskflow.dto.ApiResponse;
import com.sourav.taskflow.dto.projectMembers.ProjectMemberResponse;
import com.sourav.taskflow.service.ProjectMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectMemberController {

    private final ProjectMemberService projectMemberService;

    @PostMapping("/{projectId}/members/{memberId}")
    public ResponseEntity<ApiResponse<String>> addMember(@PathVariable Long projectId, @PathVariable Long memberId) {
        projectMemberService.addMember(projectId, memberId);
        return ResponseEntity.ok(new ApiResponse<>("Member " + memberId + " Has Been Added Successfully To Project " + projectId));
    }

    @GetMapping("/{projectId}/members")
    public ResponseEntity<ApiResponse<Page<ProjectMemberResponse>>> getProjectMembers(@PathVariable Long projectId, Pageable pageable) {
        return ResponseEntity.ok(new ApiResponse<>(projectMemberService.getProjectMembers(projectId, pageable), "Project Members Fetched Successfully"));
    }

    @PutMapping("/{projectId}/members/{memberId}")
    public ResponseEntity<ApiResponse<String>> deleteMember(@PathVariable Long projectId, @PathVariable Long memberId) {
        projectMemberService.removeMember(projectId, memberId);
        return ResponseEntity.ok(new ApiResponse<>("Member: " + memberId + " Has Been Deleted Successfully From Project " + projectId));
    }

    @PutMapping("/{projectId}/members/restore/{memberId}")
    public ResponseEntity<ApiResponse<String>> restoreMember(@PathVariable Long projectId, @PathVariable Long memberId) {
        projectMemberService.restoreMember(projectId, memberId);
        return ResponseEntity.ok(new ApiResponse<>("Member: " + memberId + " Has Been Restored Successfully From Project " + projectId));
    }


}
