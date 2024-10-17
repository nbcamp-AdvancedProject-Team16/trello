package com.sparta.springtrello.domain.attachment.repository;

import com.sparta.springtrello.domain.attachment.entity.AttachmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttachmentRepository extends JpaRepository<AttachmentEntity, Long> {
    List<AttachmentEntity> findByCard_Id(Long cardId);
}