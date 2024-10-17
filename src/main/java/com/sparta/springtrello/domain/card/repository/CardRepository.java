package com.sparta.springtrello.domain.card.repository;

import com.sparta.springtrello.domain.card.entity.CardEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CardRepository extends JpaRepository<CardEntity,Long>, CardRepositoryCustom {
    Optional<CardEntity> findByListIdAndId(Long listId, Long cardId);

}
