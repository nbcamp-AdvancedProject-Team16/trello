package com.sparta.springtrello.domain.card.repository;

import com.sparta.springtrello.domain.card.dto.CardSearchResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface CardRepositoryCustom {

    Page<CardSearchResponse> searchCards(
            Pageable pageable,
            String title,
            String description,
            LocalDate dueDate,
            String assignee
    );
}
