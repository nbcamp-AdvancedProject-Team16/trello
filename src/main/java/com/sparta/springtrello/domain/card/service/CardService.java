package com.sparta.springtrello.domain.card.service;

import com.sparta.springtrello.domain.activity.service.ActivityLogService;
import com.sparta.springtrello.domain.assignee.entity.AssigneeEntity;
import com.sparta.springtrello.domain.assignee.repository.AssigneeRepository;
import com.sparta.springtrello.domain.card.dto.CardRequest;
import com.sparta.springtrello.domain.card.dto.CardResponse;
import com.sparta.springtrello.domain.card.entity.CardEntity;
import com.sparta.springtrello.domain.card.repository.CardRepository;
import com.sparta.springtrello.domain.comment.entity.CommentEntity;
import com.sparta.springtrello.domain.comment.repository.CommentRepository;
import com.sparta.springtrello.domain.common.exception.CustomException;
import com.sparta.springtrello.domain.list.entity.ListEntity;
import com.sparta.springtrello.domain.list.repository.ListRepository;
import com.sparta.springtrello.domain.member.entity.MemberEntity;
import com.sparta.springtrello.domain.member.enums.MemberRole;
import com.sparta.springtrello.domain.member.repository.MemberRepository;
import com.sparta.springtrello.domain.notification.service.NotificationService;
import com.sparta.springtrello.domain.user.entity.CustomUserDetails;
import com.sparta.springtrello.domain.user.entity.UserEntity;
import jakarta.persistence.Cacheable;
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
@Cacheable
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;
    private final ListRepository listRepository;
    private final ActivityLogService activityLogService;
    private final AssigneeRepository assigneeRepository;
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final NotificationService notificationService;

    private final RedisTemplate<String, Object> redisTemplate;

    @Transactional
    public CardResponse createCard(CustomUserDetails authUser,Long listId, CardRequest cardRequest) {
        UserEntity user = UserEntity.fromAuthUser(authUser);

        ListEntity list = listRepository.findById(listId).orElseThrow(()-> new CustomException(404, "리스트 ID를 찾을 수 없습니다."));

        MemberEntity member = memberRepository.findByUserIdAndWorkspaceId(user.getId(), list.getBoard().getWorkspace().getId())
                .orElseThrow(() -> new CustomException(403, "해당 워크스페이스의 멤버가 아닙니다."));

        validatePermission(member);

        CardEntity card = new CardEntity(cardRequest,list);

        CardEntity savedCard = cardRepository.save(card);

        activityLogService.saveLog("카드가 생성되었습니다. 카드 ID: " + savedCard.getId());

        // 슬랙 알림 전송
        String message = String.format("%s님이 카드[%s]를 생성했습니다.", user.getEmail(), savedCard.getTitle());
        notificationService.createNotification(message,  "card");

        return new CardResponse(savedCard.getTitle(), savedCard.getDescription(), savedCard.getDueDate());
    }

    @Transactional
    public CardResponse getCard(CustomUserDetails authUser, Long cardId) {
        UserEntity user = UserEntity.fromAuthUser(authUser);

        CardEntity card = cardRepository.findById(cardId).orElseThrow(() -> new CustomException(404, "카드가 존재하지 않습니다."));

        Long workspaceId = card.getList().getBoard().getWorkspace().getId();

        MemberEntity member = memberRepository.findByUserIdAndWorkspaceId(user.getId(), workspaceId)
                .orElseThrow(() -> new CustomException(403, "해당 워크스페이스의 멤버가 아닙니다."));

        validatePermission(member);

        // 조회수 증가 및 어뷰징 방지
        incrementCardViewCount(cardId, user.getId());

        // 인기 카드 업데이트
        updatePopularCardRanking(String.valueOf(cardId));

        List<AssigneeEntity> assigneeEntity = assigneeRepository.findByCardId(cardId);
        List<CommentEntity> commentEntities = commentRepository.findByCardId(cardId);

        // 슬랙 알림 전송
        String message = String.format("%s님이 카드[%s]를 조회했습니다.", user.getEmail(), card.getTitle());
        notificationService.createNotification(message,  "card");

        activityLogService.saveLog("카드가 조회되었습니다. 카드 ID: " + card.getId());

        return new CardResponse(card,assigneeEntity,"카드 조회",commentEntities);  //todo 로그,댓글,첨부파일, 담당자 빠져있음.
    }

    @Transactional
    public CardResponse updateCard(CustomUserDetails authUser,Long listId, Long cardId, CardRequest cardRequest) {
        UserEntity user = UserEntity.fromAuthUser(authUser);

        ListEntity list = listRepository.findById(listId).orElseThrow(()-> new CustomException(404, "리스트 ID를 찾을 수 없습니다."));

        CardEntity card = cardRepository.findByListIdAndId(listId, cardId)
                .orElseThrow(() -> new CustomException(404, "해당 카드를 찾을 수 없습니다."));

        MemberEntity member = memberRepository.findByUserIdAndWorkspaceId(user.getId(), list.getBoard().getWorkspace().getId())
                .orElseThrow(() -> new CustomException(403, "해당 워크스페이스의 멤버가 아닙니다."));

        validatePermission(member);

        try {
            card.updateCard(cardRequest);

            activityLogService.saveLog("카드가 수정되었습니다. 카드 ID: " + card.getId());

            // 슬랙 알림 전송
            String message = String.format("%s님이 카드[%s]를 수정했습니다.", user.getEmail(), card.getTitle());
            notificationService.createNotification(message,  "card");

            return new CardResponse(card.getTitle(), card.getDescription(), card.getDueDate());
        } catch (OptimisticLockException e) {
            throw new CustomException(409, "다른 사용자가 이미 이 카드를 수정했습니다. 다시 시도해주세요.");
        }
    }

    @Transactional
    public void deleteCard(CustomUserDetails authUser,Long listId, Long cardId) {
        UserEntity user = UserEntity.fromAuthUser(authUser);

        ListEntity list = listRepository.findById(listId).orElseThrow(()-> new CustomException(404, "리스트 ID를 찾을 수 없습니다."));

        CardEntity card = cardRepository.findByListIdAndId(listId,cardId)
                .orElseThrow(() -> new CustomException(404, "해당 카드를 찾을 수 없습니다."));

        MemberEntity member = memberRepository.findByUserIdAndWorkspaceId(user.getId(), list.getBoard().getWorkspace().getId())
                .orElseThrow(() -> new CustomException(403, "해당 워크스페이스의 멤버가 아닙니다."));

        validatePermission(member);

        activityLogService.saveLog("카드가 삭제되었습니다. 카드 ID: " + card.getId());

        // 슬랙 알림 전송
        String message = String.format("%s님이 카드[%s]를 삭제했습니다.", user.getEmail(), card.getTitle());
        notificationService.createNotification(message,  "card");

        cardRepository.delete(card);
    }

    // 카드 조회수 증가
    public void incrementCardViewCount(Long cardId, Long userId) {
        if (cardId == null || userId == null) {
            throw new CustomException(400, "카드가 없거나 존재하지 않는 유저입니다.");
        }

        String cardViewKey = "card: " + cardId + ":views";
        String userViewKey = "user: " + userId + ":cards:" + cardId;

        if (Boolean.FALSE.equals(redisTemplate.hasKey(userViewKey))) {
            redisTemplate.opsForValue().increment(cardViewKey);
            redisTemplate.opsForValue().set(userViewKey, "1", 1, TimeUnit.DAYS);
        }
    }

    // 인기 카드 랭킹 업데이트
    public void updatePopularCardRanking(String cardId) {
        String rankingKey = "card:ranking";
        redisTemplate.opsForZSet().incrementScore(rankingKey, cardId, 1);
    }

    // 인기 카드 목록 조회
    public Set<Object> getPopularCards() {
        String rankingKey = "card:ranking";
        return redisTemplate.opsForZSet().range(rankingKey, 0, 9); // 상위 10개 카드 조회
    }

    // 자정에 조회수 리셋
    @Scheduled(cron = "0 0 0 * * ?")
    public void resetDailyCardViewCounts() {
        String rankingKey = "card:ranking";
        Set<String> cardKeys = redisTemplate.keys("card:*:views");

        if (cardKeys != null && !cardKeys.isEmpty()) {
            for (String key : cardKeys) {
                redisTemplate.delete(key);
            }
        }

        redisTemplate.delete(rankingKey);
    }

    private void validatePermission(MemberEntity member) {
        if (member.getMemberRole() == MemberRole.READ_ONLY) {
            throw new CustomException(403, "읽기 전용 역할을 가진 멤버는 리스트를 생성, 수정, 삭제할 수 없습니다.");
        }
    }
}
