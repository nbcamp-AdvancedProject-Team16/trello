package com.sparta.springtrello.domain.search.controller;

import com.sparta.springtrello.domain.search.dto.response.CardSearchResponse;
import com.sparta.springtrello.domain.search.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @GetMapping("/searches")
    public ResponseEntity<Page<CardSearchResponse>> searchCards(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "null") String title,
            @RequestParam(defaultValue = "null") String description,
            @RequestParam(defaultValue = "null") Date dueDate,
            @RequestParam(defaultValue = "null") String assignee
            ) {
        Page<CardSearchResponse> cards = searchService.getCards(page, size, title, description, dueDate, assignee);
        return ResponseEntity.ok(cards);
    }
}
