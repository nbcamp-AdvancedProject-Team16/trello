package com.sparta.springtrello.domain.list.controller;

import com.sparta.springtrello.domain.common.exception.CustomException;
import com.sparta.springtrello.domain.common.response.ApiResponse;
import com.sparta.springtrello.domain.list.dto.request.ListRequest;
import com.sparta.springtrello.domain.list.dto.response.ListResponse;
import com.sparta.springtrello.domain.list.service.ListService;
import com.sparta.springtrello.domain.user.entity.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/boards/{boardId}/lists")
@RequiredArgsConstructor
public class ListController {

    private final ListService listService;

    @PostMapping
    public ResponseEntity<ApiResponse<ListResponse>> createList(
            @PathVariable Long boardId,
            @AuthenticationPrincipal CustomUserDetails authUser,
            @RequestBody ListRequest listRequest) {
        ListResponse response = listService.createList(boardId, authUser, listRequest);
        return ResponseEntity.ok(new ApiResponse<>(200, "리스트가 성공적으로 생성되었습니다.", response));
    }

    @PatchMapping("/{listId}")
    public ResponseEntity<ApiResponse<ListResponse>> updateList(
            @PathVariable Long boardId,
            @PathVariable Long listId,
            @AuthenticationPrincipal CustomUserDetails authUser,
            @RequestBody ListRequest listRequest) {
        ListResponse response = listService.updateList(boardId, listId, authUser, listRequest);
        return ResponseEntity.ok(new ApiResponse<>(200, "리스트가 성공적으로 수정되었습니다.", response));
    }

    @DeleteMapping("/{listId}")
    public ResponseEntity<ApiResponse<Void>> deleteList(
            @PathVariable Long boardId,
            @PathVariable Long listId,
            @AuthenticationPrincipal CustomUserDetails authUser) {
        listService.deleteList(boardId, listId, authUser);
        return ResponseEntity.ok(new ApiResponse<>(200, "리스트가 성공적으로 삭제되었습니다.", null));
    }
}
