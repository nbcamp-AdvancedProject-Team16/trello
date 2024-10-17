package com.sparta.springtrello.domain.search.repository;

import com.sparta.springtrello.domain.search.dto.response.CardSearchResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;

public interface SearchRepositoryCustom {

    Page<CardSearchResponse> searchCards(
            Pageable pageable,
            String title,
            String description,
            Date Duedate,
            String assignee
    );
}
