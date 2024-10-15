package com.sparta.springtrello.domain.assignee.dto;

import com.sparta.springtrello.domain.assignee.entity.AssigneeEntity;
import lombok.Getter;

@Getter
public class AssigneeResponse {
    private Long id;

    public AssigneeResponse(AssigneeEntity assigneeEntity) {
        this.id = assigneeEntity.getId();
    }
}
