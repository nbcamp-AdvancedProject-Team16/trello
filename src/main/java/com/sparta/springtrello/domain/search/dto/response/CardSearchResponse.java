package com.sparta.springtrello.domain.search.dto.response;

import lombok.Getter;

@Getter
public class CardSearchResponse {

    private final long id;
    private final String title;         // 카드 제목


    public CardSearchResponse(long id, String title) {
        this.id = id;
        this.title = title;
    }
}
