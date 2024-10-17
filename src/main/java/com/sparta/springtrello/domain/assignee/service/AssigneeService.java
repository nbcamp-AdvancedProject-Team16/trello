package com.sparta.springtrello.domain.assignee.service;

import com.sparta.springtrello.domain.assignee.dto.AssigneeRequest;
import com.sparta.springtrello.domain.assignee.dto.AssigneeResponse;
import com.sparta.springtrello.domain.assignee.entity.AssigneeEntity;
import com.sparta.springtrello.domain.assignee.repository.AssigneeRepository;
import com.sparta.springtrello.domain.card.entity.CardEntity;
import com.sparta.springtrello.domain.card.repository.CardRepository;
import com.sparta.springtrello.domain.common.exception.CustomException;
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

    public AssigneeResponse createAssignee(Long cardId, Long userId) {
        CardEntity card = cardRepository.findById(cardId)
                .orElseThrow(() -> new CustomException(404, "카드를 찾을 수 없습니다."));


        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(404, "사용자를 찾을 수 없습니다."));


        if (assigneeRepository.existsByCardAndUser(card, user)) {
            throw new CustomException(400, "이미 해당 사용자는 이 카드의 담당자입니다.");
        }

        AssigneeEntity assignee = new AssigneeEntity(card, user);
        String message = String.format("%d번 담당자가 생성되었습니다.", userId);

        AssigneeEntity assigneeEntity = assigneeRepository.save(assignee);

        return new AssigneeResponse(assigneeEntity, message);
    }

    public AssigneeResponse getAssignee(Long cardId, Long userId) {
        AssigneeEntity assignee = assigneeRepository.findByCardIdAndUserId(cardId, userId)
                .orElseThrow(() -> new CustomException(404, "담당자를 찾을 수 없습니다."));

        String message = String.format("%d번 담당자가 조회되었습니다.", userId);

        return new AssigneeResponse(assignee, message);
    }

    public void removeAssignee(Long cardId, Long assigneeId) {
        AssigneeEntity assignee = assigneeRepository.findByIdAndCardId(assigneeId,cardId)
                .orElseThrow(() -> new CustomException(404, "담당자를 찾을 수 없습니다."));

        assigneeRepository.delete(assignee);
    }
}