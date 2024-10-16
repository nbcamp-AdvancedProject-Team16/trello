package com.sparta.springtrello.domain.comment.repository;


import com.sparta.springtrello.domain.comment.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<CommentEntity,Long> {
    Optional<CommentEntity> findByCardIdAndId(Long cardId, Long commentId);
}
