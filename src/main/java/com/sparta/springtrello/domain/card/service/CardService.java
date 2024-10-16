package com.sparta.springtrello.domain.card.service;

import com.sparta.springtrello.domain.activity.repository.ActivityLogRepository;
import com.sparta.springtrello.domain.activity.service.ActivityLogService;
import com.sparta.springtrello.domain.assignee.entity.AssigneeEntity;
import com.sparta.springtrello.domain.assignee.repository.AssigneeRepository;
import com.sparta.springtrello.domain.card.dto.CardRequest;
import com.sparta.springtrello.domain.card.dto.CardResponse;
import com.sparta.springtrello.domain.card.entity.CardEntity;
import com.sparta.springtrello.domain.card.repository.CardRepository;
import com.sparta.springtrello.domain.comment.entity.CommentEntity;
import com.sparta.springtrello.domain.comment.repository.CommentRepository;
import com.sparta.springtrello.domain.list.entity.ListEntity;
import com.sparta.springtrello.domain.list.repository.ListReposiotory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;
    private final ListReposiotory listRepository;
    private final ActivityLogService activityLogService;
    private final AssigneeRepository assigneeRepository;
    private final CommentRepository commentRepository;

    public CardResponse createCard(Long listId,CardRequest cardRequest) {

        ListEntity list = listRepository.findById(listId).orElseThrow(()-> new RuntimeException("리스트 ID를 찾을 수 없습니다."));

        CardEntity card = new CardEntity(cardRequest);

        card.listConnect(list);

        CardEntity savedCard = cardRepository.save(card);

        activityLogService.saveLog("카드가 생성되었습니다. 카드 ID: " + savedCard.getId());

        CardResponse cardResponse =  new CardResponse(savedCard);

        cardResponse.message("카드가 생성되었습니다.");

        return cardResponse;
    }

    public CardResponse getCard(Long listId, Long cardId) {
        CardEntity card = cardRepository.findByListIdAndId(listId,cardId).orElseThrow(() -> new RuntimeException("카드가 존재하지 않습니다."));


        List<AssigneeEntity> assigneeEntityList = assigneeRepository.findByCardId(cardId);
        List<CommentEntity> commentEntityList =  commentRepository.findByCardId(cardId);
        activityLogService.saveLog("카드가 조회되었습니다. 카드 ID: " + card.getId());

        CardResponse cardResponse = new CardResponse(card,assigneeEntityList,"카드를 조회했습니다.",commentEntityList);

        return cardResponse;
    }

    public CardResponse updateCard(Long listId, Long cardId, CardRequest cardRequest) {
        CardEntity card = cardRepository.findByListIdAndId(listId,cardId)
                .orElseThrow(() -> new RuntimeException("Card not found"));

        card.updateCard(cardRequest);


        activityLogService.saveLog("카드가 수정되었습니다. 카드 ID: " + card.getId());

        CardResponse cardResponse = new CardResponse(card);
        cardResponse.message("카드를 수정했습니다.");
        return cardResponse;
    }

    public void deleteCard(Long listId, Long cardId) {
        CardEntity card = cardRepository.findByListIdAndId(listId,cardId)
                .orElseThrow(() -> new RuntimeException("Card not found"));

        // 활동 내역 저장 (카드 삭제)
        activityLogService.saveLog("카드가 삭제되었습니다. 카드 ID: " + card.getId());

        CardResponse cardResponse = new CardResponse(card);
        cardResponse.message("카드를 삭제했습니다.");

        cardRepository.delete(card);
    }
}
