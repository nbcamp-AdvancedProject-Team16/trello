package com.sparta.springtrello.domain.comment.service;

import com.sparta.springtrello.domain.card.entity.CardEntity;
import com.sparta.springtrello.domain.card.repository.CardRepository;
import com.sparta.springtrello.domain.comment.dto.CommentRequest;
import com.sparta.springtrello.domain.comment.dto.CommentResponse;
import com.sparta.springtrello.domain.comment.entity.CommentEntity;
import com.sparta.springtrello.domain.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final CardRepository cardRepository;


    public CommentResponse createComment(Long cardId,CommentRequest commentRequest) {
        CardEntity card = cardRepository.findById(cardId).orElseThrow(() -> new RuntimeException("카드를 찾을 수 없습니다."));

        CommentEntity comment = new CommentEntity(commentRequest,card);

        CommentEntity savedComment = commentRepository.save(comment);

        CommentResponse response = new CommentResponse(savedComment);

        response.message("댓글이 생성되었습니다.");

        return response;
    }

    public CommentResponse updateComment(Long cardId,Long commentId, CommentRequest commentRequest) {
        CommentEntity comment = commentRepository.findByCardIdAndId(cardId, commentId)
                .orElseThrow(() -> new RuntimeException("해당 카드에 댓글이 존재하지 않습니다."));

        comment.update(commentRequest);

        CommentEntity updatedComment = commentRepository.save(comment);

        CommentResponse response = new CommentResponse(updatedComment);

        response.message("댓글이 수정되었습니다.");

        return response;
    }

    public void deleteComment(Long cardId,Long commentId) {
        CommentEntity comment = commentRepository.findByCardIdAndId(cardId, commentId)
                .orElseThrow(() -> new RuntimeException("해당 카드에 댓글이 존재하지 않습니다."));

        commentRepository.delete(comment);

    }
}
