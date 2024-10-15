package com.sparta.springtrello.domain.board.controller;

import com.sparta.springtrello.domain.board.dto.request.BoardRequest;
import com.sparta.springtrello.domain.board.dto.response.BoardResponse;
import com.sparta.springtrello.domain.board.service.BoardService;
import com.sparta.springtrello.domain.common.exception.CustomException;
import com.sparta.springtrello.domain.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/workspace/{workspaceId}/boards")
public class BoardController {

    private final BoardService boardService;

    @PostMapping
    public ResponseEntity<ApiResponse<BoardResponse>> createBoard(
            @PathVariable Long workspaceId,
            @RequestParam Long memberId,
            @RequestBody BoardRequest boardRequest) {
        try {
            BoardResponse response = boardService.createBoard(workspaceId, memberId, boardRequest);
            return ResponseEntity.ok(new ApiResponse<>(200, "정상처리되었습니다.", response));
        } catch (CustomException e) {
            return ResponseEntity.status(e.getStatus())
                    .body(new ApiResponse<>(e.getStatus(), e.getMessage(), null));
        }
    }

    @PatchMapping("/{boardId}")
    public ResponseEntity<ApiResponse<BoardResponse>> updateBoard(
            @PathVariable Long workspaceId,
            @PathVariable Long boardId,
            @RequestParam Long memberId,
            @RequestBody BoardRequest boardRequest) {
        try {
            BoardResponse response = boardService.updateBoard(boardId, memberId, boardRequest);
            return ResponseEntity.ok(new ApiResponse<>(200, "정상처리되었습니다.", response));
        } catch (CustomException e) {
            return ResponseEntity.status(e.getStatus())
                    .body(new ApiResponse<>(e.getStatus(), e.getMessage(), null));
        }
    }

    @DeleteMapping("/{boardId}")
    public ResponseEntity<ApiResponse<Void>> deleteBoard(
            @PathVariable Long workspaceId,
            @PathVariable Long boardId,
            @RequestParam Long memberId) {
        try {
            boardService.deleteBoard(boardId, memberId);
            return ResponseEntity.ok(new ApiResponse<>(200, "보드가 성공적으로 삭제되었습니다.", null));
        } catch (CustomException e) {
            return ResponseEntity.status(e.getStatus())
                    .body(new ApiResponse<>(e.getStatus(), e.getMessage(), null));
        }
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<ApiResponse<BoardResponse>> getBoard(
            @PathVariable Long workspaceId,
            @PathVariable Long boardId,
            @RequestParam Long memberId) {
        try {
            BoardResponse response = boardService.getBoard(boardId, memberId);
            return ResponseEntity.ok(new ApiResponse<>(200, "정상처리되었습니다.", response));
        } catch (CustomException e) {
            return ResponseEntity.status(e.getStatus())
                    .body(new ApiResponse<>(e.getStatus(), e.getMessage(), null));
        }
    }
}
