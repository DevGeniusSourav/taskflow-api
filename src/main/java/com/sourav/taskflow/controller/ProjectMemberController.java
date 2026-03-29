package com.sourav.taskflow.controller;

import com.sourav.taskflow.dto.projectMembers.ProjectMemberResponse;
import com.sourav.taskflow.entity.ProjectMember;
import com.sourav.taskflow.service.ProjectMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectMemberController {

    private final ProjectMemberService projectMemberService;

    @PostMapping("/{projectId}/members/{memberId}")
    public ResponseEntity<String> addMember(@PathVariable Long projectId, @PathVariable Long memberId) {
        projectMemberService.addMember(projectId, memberId);
        return ResponseEntity.ok("Member " + memberId + " Has Been Added Successfully To Project " + projectId);
    }

    @GetMapping("/{projectId}/members")
    public ResponseEntity<Page<ProjectMemberResponse>> getProjectMembers(@PathVariable Long projectId, Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(projectMemberService.getProjectMembers(projectId, pageable));
    }

    @PutMapping("/{projectId}/members/{memberId}")
    public ResponseEntity<String> deleteMember(@PathVariable Long projectId, @PathVariable Long memberId) {
        projectMemberService.removeMember(projectId, memberId);
        return ResponseEntity.ok("Member: " + memberId + " Has Been Deleted Successfully From Project " + projectId);
    }

    @PutMapping("/{projectId}/members/restore/{memberId}")
    public ResponseEntity<String> restoreMember(@PathVariable Long projectId, @PathVariable Long memberId) {
        projectMemberService.restoreMember(projectId, memberId);
        return ResponseEntity.ok("Member: " + memberId + " Has Been Restored Successfully From Project " + projectId);
    }



}
