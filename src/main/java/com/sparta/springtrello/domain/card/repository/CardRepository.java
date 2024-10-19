package com.sparta.springtrello.domain.card.repository;

import com.sparta.springtrello.domain.card.entity.CardEntity;
import io.lettuce.core.dynamic.annotation.Param;
import jakarta.persistence.Entity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CardRepository extends JpaRepository<CardEntity,Long>, CardRepositoryCustom {

    Optional<CardEntity> findByListIdAndId(@Param("listId") Long listId, @Param("cardId") Long cardId);
}