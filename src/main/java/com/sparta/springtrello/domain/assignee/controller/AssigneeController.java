package com.sparta.springtrello.domain.assignee.controller;

import com.sparta.springtrello.domain.assignee.dto.AssigneeRequest;
import com.sparta.springtrello.domain.assignee.dto.AssigneeResponse;
import com.sparta.springtrello.domain.assignee.service.AssigneeService;
import com.sparta.springtrello.domain.common.response.ApiResponse;
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
    public ResponseEntity<ApiResponse<AssigneeResponse>> createAssignee(@PathVariable Long cardId,@PathVariable Long userId) {
        AssigneeResponse response = assigneeService.createAssignee(cardId,userId);
        return ResponseEntity.ok(new ApiResponse<>(201, "담당자가 성공적으로 생성되었습니다.", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<AssigneeResponse>> getAssignee(@PathVariable Long cardId, @PathVariable Long userId) {
        AssigneeResponse response = assigneeService.getAssignee(cardId,userId);

        return ResponseEntity.ok(new ApiResponse<>(200, "담당자가 성공적으로 수정되었습니다.", response));
    }

    @DeleteMapping("/{assigneeId}")
    public ResponseEntity<ApiResponse<Void>> removeAssignee(@PathVariable Long cardId, @PathVariable Long userId,@PathVariable Long assigneeId) {
        assigneeService.removeAssignee(cardId, assigneeId);
        return ResponseEntity.ok(new ApiResponse<>(200, "보드가 성공적으로 삭제되었습니다.", null));
    }
}