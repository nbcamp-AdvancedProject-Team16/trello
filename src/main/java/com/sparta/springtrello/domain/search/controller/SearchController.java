package com.sparta.springtrello.domain.search.controller;

import com.sparta.springtrello.domain.search.dto.response.CardSearchResponse;
import com.sparta.springtrello.domain.search.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @GetMapping("/searches")
    public ResponseEntity<Page<CardSearchResponse>> searchCards(
            @RequestParam(defaultValue = "null") String title,
            @RequestParam()
    ) {

    }
}
