package com.sparta.springtrello.domain.assignee.dto;

import com.sparta.springtrello.domain.assignee.entity.AssigneeEntity;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class AssigneeResponse {
    private Long id;
    private String username;
    private String message;
    private LocalDateTime assignedAt;


    public AssigneeResponse(AssigneeEntity assigneeEntity,String message) {
        this.id = assigneeEntity.getId();
        this.username = assigneeEntity.getUser().getUsername();
        this.message = message;
        this.assignedAt = assigneeEntity.getAssignedAt();
    }
}
