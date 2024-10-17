package com.sparta.springtrello.domain.comment.service;

import com.sparta.springtrello.domain.card.entity.CardEntity;
import com.sparta.springtrello.domain.card.repository.CardRepository;
import com.sparta.springtrello.domain.comment.dto.CommentRequest;
import com.sparta.springtrello.domain.comment.dto.CommentResponse;
import com.sparta.springtrello.domain.comment.entity.CommentEntity;
import com.sparta.springtrello.domain.comment.repository.CommentRepository;
import com.sparta.springtrello.domain.common.exception.CustomException;
import com.sparta.springtrello.domain.member.entity.MemberEntity;
import com.sparta.springtrello.domain.member.enums.MemberRole;
import com.sparta.springtrello.domain.member.repository.MemberRepository;
import com.sparta.springtrello.domain.notification.service.NotificationService;
import com.sparta.springtrello.domain.user.entity.CustomUserDetails;
import com.sparta.springtrello.domain.user.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final CardRepository cardRepository;
    private final MemberRepository memberRepository;
    private final NotificationService notificationService;

    //워크스페이스 멤버인지아닌지 체크 -> readonly
    @Transactional
    public CommentResponse createComment(CustomUserDetails authUser,Long cardId, CommentRequest commentRequest) {
        UserEntity user = UserEntity.fromAuthUser(authUser);

        CardEntity card = cardRepository.findById(cardId).orElseThrow(() -> new CustomException(404, "카드를 찾을 수 없습니다."));

        MemberEntity member = memberRepository.findByUserIdAndWorkspaceId(user.getId(), card.getList().getBoard().getWorkspace().getId())
                .orElseThrow(() -> new CustomException(403, "해당 워크스페이스의 멤버가 아닙니다."));

        validatePermission(member);

        CommentEntity comment = new CommentEntity(commentRequest,card);

        CommentEntity savedComment = commentRepository.save(comment);

        // 슬랙 알림 전송
        String message = String.format("%s님이 카드[%s]에 댓글을 작성했습니다: %s", user.getEmail(), card.getTitle(), comment.getText());
        notificationService.createNotification(message, "comment");

        CommentResponse response = new CommentResponse(savedComment);

        response.message("댓글이 생성되었습니다.");

        return response;
    }
    @Transactional
    public CommentResponse updateComment(CustomUserDetails authUser,Long cardId,Long commentId, CommentRequest commentRequest) {

        UserEntity user = UserEntity.fromAuthUser(authUser);

        CommentEntity comment = commentRepository.findByCardIdAndId(cardId, commentId)
                .orElseThrow(() -> new CustomException(404, "해당 카드에 댓글이 존재하지 않습니다.")); //워크스페이스멤버인지 권한 확인

        // 카드 정보 가져오기
        CardEntity card = comment.getCard(); // 댓글이 속한 카드 가져오기

        //읽기 전용 역할을 가진 멤버가 카드를 수정하려는 경우
        if (!comment.isCreatedBy(user)) {
            throw new CustomException(403, "댓글을 수정할 권한이 없습니다.");
        }

        comment.update(commentRequest);

        CommentEntity updatedComment = commentRepository.save(comment);

        // 슬랙 알림 전송
        String message = String.format("%s님이 카드[%s]의 댓글을 수정했습니다: %s", user.getEmail(), card.getTitle(), commentRequest.getText());
        notificationService.createNotification(message,  "comment");

        CommentResponse response = new CommentResponse(updatedComment);

        response.message("댓글이 수정되었습니다.");

        return response;
    }
    @Transactional
    public void deleteComment(CustomUserDetails authUser,Long cardId,Long commentId) {

        UserEntity user = UserEntity.fromAuthUser(authUser);

        CommentEntity comment = commentRepository.findByCardIdAndId(cardId, commentId)
                .orElseThrow(() -> new CustomException(404, "해당 카드에 댓글이 존재하지 않습니다."));

        // 카드 정보 가져오기
        CardEntity card = comment.getCard(); // 댓글이 속한 카드 가져오기

        //읽기 전용 역할을 가진 멤버가 카드를 삭제하려는 경우
        if (!comment.isCreatedBy(user)) {
            throw new CustomException(403, "댓글을 삭제할 권한이 없습니다.");
        }

        // 슬랙 알림 전송
        String message = String.format("%s님이 카드[%s]의 댓글을 삭제했습니다.", user.getEmail(), card.getTitle());
        notificationService.createNotification(message, "comment");

        commentRepository.delete(comment);

    }

    private void validatePermission(MemberEntity member) {
        if (member.getMemberRole() == MemberRole.READ_ONLY) {
            throw new CustomException(403, "읽기 전용 역할을 가진 멤버는 리스트를 생성, 수정, 삭제할 수 없습니다.");
        }
    }
}
