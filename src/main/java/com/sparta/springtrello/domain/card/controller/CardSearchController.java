package com.sparta.springtrello.domain.card.controller;

import com.sparta.springtrello.domain.card.dto.CardSearchResponse;
import com.sparta.springtrello.domain.card.service.CardSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
public class CardSearchController {

    private final CardSearchService cardSearchService;

    @GetMapping("/searches")
    public ResponseEntity<Page<CardSearchResponse>> searchCards(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) LocalDate dueDate,
            @RequestParam(required = false) String assignee
    ) {
        Page<CardSearchResponse> cards = cardSearchService.searchCards(page, size, title, description, dueDate, assignee);
        return ResponseEntity.ok(cards);
    }
}

