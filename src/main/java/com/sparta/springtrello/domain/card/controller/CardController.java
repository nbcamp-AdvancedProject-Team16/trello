package com.sparta.springtrello.domain.card.controller;

import com.sparta.springtrello.domain.card.dto.CardRequest;
import com.sparta.springtrello.domain.card.dto.CardResponse;
import com.sparta.springtrello.domain.card.service.CardService;
import com.sparta.springtrello.domain.user.entity.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/lists")
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;

    @PostMapping("/{listId}/cards")
    public ResponseEntity<CardResponse> createCard(@AuthenticationPrincipal CustomUserDetails authUser,@PathVariable Long listId, @RequestBody CardRequest cardRequest) {
       CardResponse response = cardService.createCard(authUser,listId,cardRequest);

       return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{cardId}")
    public ResponseEntity<CardResponse> getCard(@AuthenticationPrincipal CustomUserDetails authUser,@PathVariable Long cardId) {

        CardResponse response = cardService.getCard(authUser,cardId);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @PatchMapping("/{listId}/cards/{cardId}")
    public ResponseEntity<CardResponse> updateCard(@AuthenticationPrincipal CustomUserDetails authUser,@PathVariable Long listId, @PathVariable Long cardId, @RequestBody CardRequest cardRequest) {
        CardResponse response = cardService.updateCard(authUser,listId,cardId,cardRequest);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @DeleteMapping("/{listId}/cards/{cardId}")
    public ResponseEntity<Void> deleteCard(@AuthenticationPrincipal CustomUserDetails authUser,@PathVariable Long listId, @PathVariable Long cardId) {
        cardService.deleteCard(authUser,listId,cardId);

        return new ResponseEntity<Void>(HttpStatus.OK);
    }


}
