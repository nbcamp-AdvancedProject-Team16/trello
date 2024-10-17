package com.sparta.springtrello.domain.card.service;

import com.sparta.springtrello.domain.card.dto.CardSearchResponse;
import com.sparta.springtrello.domain.card.repository.CardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class CardSearchService {

    private final CardRepository cardRepository;

    public Page<CardSearchResponse> getCards(
            int page, int size, String title, String description, LocalDate dueDate, String assignee) {

        Pageable pageable = PageRequest.of(page-1, size);

        return cardRepository.searchCards(pageable, title, description, dueDate, assignee);
    }
}
