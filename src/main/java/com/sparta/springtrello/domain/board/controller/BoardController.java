package com.sparta.springtrello.domain.board.controller;

import com.sparta.springtrello.domain.board.dto.request.BoardRequest;
import com.sparta.springtrello.domain.board.dto.response.BoardResponse;
import com.sparta.springtrello.domain.board.service.BoardService;
import com.sparta.springtrello.domain.common.exception.CustomException;
import com.sparta.springtrello.domain.common.response.ApiResponse;
import com.sparta.springtrello.domain.user.entity.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/workspaces/{workspaceId}/boards")
public class BoardController {

    private final BoardService boardService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<BoardResponse>> createBoard(
            @AuthenticationPrincipal CustomUserDetails authUser,
            @PathVariable Long workspaceId,
            @RequestPart("boardRequest") BoardRequest boardRequest,
            @RequestPart(value = "backgroundImage", required = false) MultipartFile backgroundImage) {
        if (authUser == null) {
            return ResponseEntity.status(403).body(new ApiResponse<>(403, "인증되지 않은 사용자입니다.", null));
        }
        try {
            BoardResponse response = boardService.createBoard(authUser, workspaceId, boardRequest, backgroundImage);
            return ResponseEntity.ok(new ApiResponse<>(200, "정상처리되었습니다.", response));
        } catch (CustomException e) {
            return ResponseEntity.status(e.getStatus())
                    .body(new ApiResponse<>(e.getStatus(), e.getMessage(), null));
        }
    }

    @PatchMapping(value = "/{boardId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<BoardResponse>> updateBoard(
            @AuthenticationPrincipal CustomUserDetails authUser,
            @PathVariable Long workspaceId,
            @PathVariable Long boardId,
            @RequestPart("boardRequest") BoardRequest boardRequest,
            @RequestPart(value = "backgroundImage", required = false) MultipartFile backgroundImage) {
        try {
            BoardResponse response = boardService.updateBoard(boardId, workspaceId, authUser, boardRequest, backgroundImage);
            return ResponseEntity.ok(new ApiResponse<>(200, "정상처리되었습니다.", response));
        } catch (CustomException e) {
            return ResponseEntity.status(e.getStatus())
                    .body(new ApiResponse<>(e.getStatus(), e.getMessage(), null));
        }
    }

    @DeleteMapping("/{boardId}")
    public ResponseEntity<ApiResponse<Void>> deleteBoard(
            @PathVariable Long boardId,
            @PathVariable Long workspaceId,
            @AuthenticationPrincipal CustomUserDetails authUser) {
        try {
            boardService.deleteBoard(boardId, workspaceId, authUser);
            return ResponseEntity.ok(new ApiResponse<>(200, "보드가 성공적으로 삭제되었습니다.", null));
        } catch (CustomException e) {
            return ResponseEntity.status(e.getStatus())
                    .body(new ApiResponse<>(e.getStatus(), e.getMessage(), null));
        }
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<ApiResponse<BoardResponse>> getBoard(
            @PathVariable Long boardId,
            @PathVariable Long workspaceId,
            @AuthenticationPrincipal CustomUserDetails authUser) {
        try {
            BoardResponse response = boardService.getBoard(boardId, workspaceId, authUser);
            return ResponseEntity.ok(new ApiResponse<>(200, "정상처리되었습니다.", response));
        } catch (CustomException e) {
            return ResponseEntity.status(e.getStatus())
                    .body(new ApiResponse<>(e.getStatus(), e.getMessage(), null));
        }
    }
}
