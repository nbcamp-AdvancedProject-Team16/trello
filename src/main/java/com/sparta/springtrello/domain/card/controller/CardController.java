package com.sparta.springtrello.domain.card.controller;

import com.sparta.springtrello.domain.card.dto.CardRequest;
import com.sparta.springtrello.domain.card.dto.CardResponse;
import com.sparta.springtrello.domain.card.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/lists/{listId}/cards")
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;


    @PostMapping
    public ResponseEntity<CardResponse> createCard(@PathVariable Long listId,@RequestBody CardRequest cardRequest) {
       CardResponse response = cardService.createCard(listId,cardRequest);

       return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{cardId}")
    public ResponseEntity<CardResponse> getCard(@PathVariable Long listId, @PathVariable Long cardId) {

        CardResponse response = cardService.getCard(listId,cardId);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @PatchMapping("/{cardId}")
    public ResponseEntity<CardResponse> updateCard(@PathVariable Long listId, @PathVariable Long cardId, @RequestBody CardRequest cardRequest) {
        CardResponse response = cardService.updateCard(listId,cardId,cardRequest);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @DeleteMapping("/{cardId}")
    public ResponseEntity<Void> deleteCard(@PathVariable Long listId, @PathVariable Long cardId) {
        cardService.deleteCard(listId,cardId);

        return new ResponseEntity<Void>(HttpStatus.OK);
    }


}
