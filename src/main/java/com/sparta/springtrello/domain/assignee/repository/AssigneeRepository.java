package com.sparta.springtrello.domain.assignee.repository;

import com.sparta.springtrello.domain.assignee.entity.AssigneeEntity;
import com.sparta.springtrello.domain.card.entity.CardEntity;
import com.sparta.springtrello.domain.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AssigneeRepository extends JpaRepository<AssigneeEntity,Long> {
    Optional<AssigneeEntity> findByIdAndCardId(Long assigneeId,Long cardId);

    boolean existsByCardAndUser(CardEntity card, UserEntity user);
}
