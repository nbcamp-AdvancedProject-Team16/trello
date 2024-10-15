package com.sparta.springtrello.domain.assignee.controller;

import com.sparta.springtrello.domain.assignee.dto.AssigneeRequest;
import com.sparta.springtrello.domain.assignee.dto.AssigneeResponse;
import com.sparta.springtrello.domain.assignee.service.AssigneeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cards/{cardId}/users/{userId}/assignees")
@RequiredArgsConstructor
public class AssigneeController {

    private final AssigneeService assigneeService;

    @PostMapping
    public ResponseEntity<AssigneeResponse> createAssignee(@PathVariable Long cardId,@PathVariable Long userId) {
        AssigneeResponse response = assigneeService.createAssignee(cardId,userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{assigneeId}")
    public ResponseEntity<Void> removeAssignee(@PathVariable Long cardId, @PathVariable Long userId,@PathVariable Long assigneeId) {
        assigneeService.removeAssignee(cardId, assigneeId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}