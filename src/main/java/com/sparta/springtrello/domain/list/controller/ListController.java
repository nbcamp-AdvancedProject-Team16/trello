package com.sparta.springtrello.domain.list.controller;

import com.sparta.springtrello.domain.common.exception.CustomException;
import com.sparta.springtrello.domain.common.response.ApiResponse;
import com.sparta.springtrello.domain.list.dto.request.ListRequest;
import com.sparta.springtrello.domain.list.dto.response.ListResponse;
import com.sparta.springtrello.domain.list.service.ListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/boards/{boardId}/lists")
@RequiredArgsConstructor
public class ListController {

    private final ListService listService;

    @PostMapping
    public ResponseEntity<ApiResponse<ListResponse>> createList(
            @PathVariable Long boardId,
            @RequestParam Long memberId,
            @RequestBody ListRequest listRequest) {
        try {
            ListResponse response = listService.createList(boardId, memberId, listRequest);
            return ResponseEntity.ok(new ApiResponse<>(200, "정상처리되었습니다.", response));
        } catch (CustomException e) {
            return ResponseEntity.status(e.getStatus())
                    .body(new ApiResponse<>(e.getStatus(), e.getMessage(), null));
        }
    }

    @PatchMapping("/{listId}")
    public ResponseEntity<ApiResponse<ListResponse>> updateList(
            @PathVariable Long boardId,
            @PathVariable Long listId,
            @RequestParam Long memberId,
            @RequestBody ListRequest listRequest) {
        try {
            ListResponse response = listService.updateList(listId, memberId, listRequest);
            return ResponseEntity.ok(new ApiResponse<>(200, "정상처리되었습니다.", response));
        } catch (CustomException e) {
            return ResponseEntity.status(e.getStatus())
                    .body(new ApiResponse<>(e.getStatus(), e.getMessage(), null));
        }
    }

    @DeleteMapping("/{listId}")
    public ResponseEntity<ApiResponse<Void>> deleteList(
            @PathVariable Long boardId,
            @PathVariable Long listId,
            @RequestParam Long memberId) {
        try {
            listService.deleteList(listId, memberId);
            return ResponseEntity.ok(new ApiResponse<>(200, "리스트가 성공적으로 삭제되었습니다.", null));
        } catch (CustomException e) {
            return ResponseEntity.status(e.getStatus())
                    .body(new ApiResponse<>(e.getStatus(), e.getMessage(), null));
        }
    }
}
