package com.sourav.taskflow.dto.projectMembers;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProjectMemberResponse {
    private Long id;
    private Long projectId;
    private String projectName;
    private Long memberId;
    private String memberName;
}
