package com.sparta.springtrello.domain.search.service;

import com.sparta.springtrello.domain.search.dto.response.CardSearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final SearchRepository searchRepository;

    public Page<CardSearchResponse> getCards(int page, int size, String title, String description, Date dueDate, String assignee) {
        Pageable pageable = PageRequest.of(page-1, size);

        return searchRepository.searchCards(pageable, title, description, dueDate, assignee);
    }


}
