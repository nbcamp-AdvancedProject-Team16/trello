package com.sparta.springtrello.domain.board.controller;

import com.sparta.springtrello.domain.board.dto.request.BoardRequest;
import com.sparta.springtrello.domain.board.dto.response.BoardResponse;
import com.sparta.springtrello.domain.board.service.BoardService;
import com.sparta.springtrello.domain.common.exception.CustomException;
import com.sparta.springtrello.domain.common.response.ApiResponse;
import com.sparta.springtrello.domain.user.entity.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/workspaces/{workspaceId}/boards")
public class BoardController {

    private final BoardService boardService;

    @PostMapping
    public ResponseEntity<ApiResponse<BoardResponse>> createBoard(
            @AuthenticationPrincipal CustomUserDetails authUser,
            @PathVariable Long workspaceId,
            @RequestBody BoardRequest boardRequest) {
        BoardResponse response = boardService.createBoard(authUser, workspaceId, boardRequest);
        return ResponseEntity.ok(new ApiResponse<>(200, "정상처리되었습니다.", response));
    }

    @PatchMapping("/{boardId}")
    public ResponseEntity<ApiResponse<BoardResponse>> updateBoard(
            @AuthenticationPrincipal CustomUserDetails authUser,
            @PathVariable Long workspaceId,
            @PathVariable Long boardId,
            @RequestBody BoardRequest boardRequest) {
        BoardResponse response = boardService.updateBoard(boardId, workspaceId, authUser, boardRequest);
        return ResponseEntity.ok(new ApiResponse<>(200, "정상처리되었습니다.", response));
    }

    @DeleteMapping("/{boardId}")
    public ResponseEntity<ApiResponse<Void>> deleteBoard(
            @PathVariable Long boardId,
            @PathVariable Long workspaceId,
            @AuthenticationPrincipal CustomUserDetails authUser) {
        boardService.deleteBoard(boardId, workspaceId, authUser);
        return ResponseEntity.ok(new ApiResponse<>(200, "보드가 성공적으로 삭제되었습니다.", null));
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<ApiResponse<BoardResponse>> getBoard(
            @PathVariable Long boardId,
            @PathVariable Long workspaceId,
            @AuthenticationPrincipal CustomUserDetails authUser) {
        BoardResponse response = boardService.getBoard(boardId, workspaceId, authUser);
        return ResponseEntity.ok(new ApiResponse<>(200, "정상처리되었습니다.", response));
    }
}

