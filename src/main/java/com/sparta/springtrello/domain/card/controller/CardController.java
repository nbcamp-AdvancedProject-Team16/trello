package com.sparta.springtrello.domain.card.controller;

import com.sparta.springtrello.domain.card.dto.CardRequest;
import com.sparta.springtrello.domain.card.dto.CardResponse;
import com.sparta.springtrello.domain.card.service.CardService;
import com.sparta.springtrello.domain.common.response.ApiResponse;
import com.sparta.springtrello.domain.user.entity.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/lists")
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;

    @PostMapping("/{listId}/cards")
    public ResponseEntity<ApiResponse<CardResponse>> createCard(@AuthenticationPrincipal CustomUserDetails authUser, @PathVariable Long listId, @RequestBody CardRequest cardRequest) {
       CardResponse response = cardService.createCard(authUser, listId, cardRequest);

       return ResponseEntity.ok(new ApiResponse<>(201, "카드가 성공적으로 생성되었습니다.", response));
    }

    @GetMapping("/{cardId}")
    public ResponseEntity<ApiResponse<CardResponse>> getCard(@AuthenticationPrincipal CustomUserDetails authUser, @PathVariable Long cardId) {

        CardResponse response = cardService.getCard(authUser, cardId);

        return ResponseEntity.ok(new ApiResponse<>(200, "카드가 성공적으로 조회되었습니다.", response));
    }

    @PatchMapping("/{listId}/cards/{cardId}")
    public ResponseEntity<ApiResponse<CardResponse>> updateCard(@AuthenticationPrincipal CustomUserDetails authUser, @PathVariable Long listId, @PathVariable Long cardId, @RequestBody CardRequest cardRequest) {
        CardResponse response = cardService.updateCard(authUser, listId, cardId, cardRequest);

        return ResponseEntity.ok(new ApiResponse<>(200, "카드가 성공적으로 수정되었습니다.", response));
    }

    @DeleteMapping("/{listId}/cards/{cardId}")
    public ResponseEntity<ApiResponse<Void>> deleteCard(@AuthenticationPrincipal CustomUserDetails authUser, @PathVariable Long listId, @PathVariable Long cardId) {
        cardService.deleteCard(authUser, listId, cardId);

        return ResponseEntity.ok(new ApiResponse<>(200, "카드가 성공적으로 삭제되었습니다.", null));
    }

    @GetMapping("/popular")
    public ResponseEntity<Set<Object>> getPopularCards() {
        Set<Object> popularCards = cardService.getPopularCards();
        return ResponseEntity.ok(popularCards);
    }
}
