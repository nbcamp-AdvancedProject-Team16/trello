package com.sparta.springtrello.domain.comment.controller;

import com.sparta.springtrello.domain.comment.dto.CommentRequest;
import com.sparta.springtrello.domain.comment.dto.CommentResponse;
import com.sparta.springtrello.domain.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/cards/{cardId}/comments")
    public ResponseEntity<CommentResponse> createComment(@PathVariable Long cardId, @RequestBody CommentRequest commentRequest) {
        CommentResponse response = commentService.createComment(cardId,commentRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/cards/{cardId}/comments/{commentId}")
    public ResponseEntity<CommentResponse> updateComment(@PathVariable Long cardId, @PathVariable Long commentId, @RequestBody CommentRequest commentRequest) {
        CommentResponse response = commentService.updateComment(cardId,commentId, commentRequest);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/cards/{cardId}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long cardId,@PathVariable Long commentId) {
        commentService.deleteComment(cardId,commentId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
