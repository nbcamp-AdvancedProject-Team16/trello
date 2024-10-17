package com.sparta.springtrello.domain.assignee.service;

import com.sparta.springtrello.domain.assignee.dto.AssigneeRequest;
import com.sparta.springtrello.domain.assignee.dto.AssigneeResponse;
import com.sparta.springtrello.domain.assignee.entity.AssigneeEntity;
import com.sparta.springtrello.domain.assignee.repository.AssigneeRepository;
import com.sparta.springtrello.domain.card.entity.CardEntity;
import com.sparta.springtrello.domain.card.repository.CardRepository;
import com.sparta.springtrello.domain.notification.service.NotificationService;
import com.sparta.springtrello.domain.user.entity.UserEntity;
import com.sparta.springtrello.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AssigneeService {

    private final AssigneeRepository assigneeRepository;
    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    public AssigneeResponse createAssignee(Long cardId, Long userId) {
        CardEntity card = cardRepository.findById(cardId)
                .orElseThrow(() -> new RuntimeException("카드를 찾을 수 없습니다."));


        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));


        if (assigneeRepository.existsByCardAndUser(card, user)) {
            throw new RuntimeException("이미 해당 사용자는 이 카드의 담당자입니다.");
        }

        AssigneeEntity assignee = new AssigneeEntity(card, user);
        String message = String.format("%d번 담당자가 생성되었습니다.", userId);

        AssigneeEntity assigneeEntity = assigneeRepository.save(assignee);

        // 슬랙 알림 전송
        String slackMessage = String.format("%s를 카드[%s]의 담당자로 추가했습니다.",
                user.getEmail(), card.getTitle());

        // 알림 생성
        notificationService.createNotification(slackMessage,"assignee");

        return new AssigneeResponse(assigneeEntity, message);
    }

    public AssigneeResponse getAssignee(Long cardId, Long userId) {
        AssigneeEntity assignee = assigneeRepository.findByCardIdAndUserId(cardId, userId)
                .orElseThrow(() -> new RuntimeException("담당자를 찾을 수 없습니다."));

        String message = String.format("%d번 담당자가 조회되었습니다.", userId);

        // 슬랙 알림 전송
        String slackMessage = String.format("카드[%s]의 담당자[%s]를 조회했습니다.",
                assignee.getCard().getTitle(), assignee.getUser().getEmail());

        // 알림 생성
        notificationService.createNotification(slackMessage, "assignee");

        return new AssigneeResponse(assignee, message);
    }

    public void removeAssignee(Long cardId, Long assigneeId) {
        AssigneeEntity assignee = assigneeRepository.findByIdAndCardId(assigneeId,cardId)
                .orElseThrow(() -> new RuntimeException("담당자를 찾을 수 없습니다."));

        // 슬랙 알림 전송
        String message = String.format("카드[%s]의 담당자[%s]가 제거되었니다.",
                assignee.getCard().getTitle(), assignee.getUser().getEmail());

        // 알림 생성
        notificationService.createNotification(message, "assignee");


        assigneeRepository.delete(assignee);
    }
}