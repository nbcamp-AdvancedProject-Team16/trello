package com.sparta.springtrello.domain.comment.controller;

import com.sparta.springtrello.domain.comment.dto.CommentRequest;
import com.sparta.springtrello.domain.comment.dto.CommentResponse;
import com.sparta.springtrello.domain.comment.service.CommentService;
import com.sparta.springtrello.domain.common.response.ApiResponse;
import com.sparta.springtrello.domain.user.entity.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/cards/{cardId}/comments")
    public ResponseEntity<ApiResponse<CommentResponse>> createComment(@AuthenticationPrincipal CustomUserDetails authUser, @PathVariable Long cardId, @RequestBody CommentRequest commentRequest) {
        CommentResponse response = commentService.createComment(authUser,cardId,commentRequest);
        return ResponseEntity.ok(new ApiResponse<>(201, "댓글이 성공적으로 생성되었습니다.", response));
    }

    @PatchMapping("/cards/{cardId}/comments/{commentId}")
    public ResponseEntity<ApiResponse<CommentResponse>> updateComment(@AuthenticationPrincipal CustomUserDetails authUser,@PathVariable Long cardId, @PathVariable Long commentId, @RequestBody CommentRequest commentRequest) {
        CommentResponse response = commentService.updateComment(authUser,cardId,commentId, commentRequest);
        return ResponseEntity.ok(new ApiResponse<>(200, "댓글이 성공적으로 생성되었습니다.", response));
    }

    @DeleteMapping("/cards/{cardId}/comments/{commentId}")
    public ResponseEntity<ApiResponse<Void>> deleteComment(@AuthenticationPrincipal CustomUserDetails authUser,@PathVariable Long cardId,@PathVariable Long commentId) {
        commentService.deleteComment(authUser,cardId,commentId);
        return ResponseEntity.ok(new ApiResponse<>(200, "댓글이 성공적으로 삭제되었습니다.", null));
    }
}
