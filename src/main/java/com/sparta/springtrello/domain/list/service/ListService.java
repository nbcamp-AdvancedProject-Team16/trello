package com.sparta.springtrello.domain.list.service;

import com.sparta.springtrello.domain.board.entity.BoardEntity;
import com.sparta.springtrello.domain.board.repository.BoardRepository;
import com.sparta.springtrello.domain.common.exception.CustomException;
import com.sparta.springtrello.domain.list.dto.request.ListRequest;
import com.sparta.springtrello.domain.list.dto.response.ListResponse;
import com.sparta.springtrello.domain.list.entity.ListEntity;
import com.sparta.springtrello.domain.list.repository.ListRepository;
import com.sparta.springtrello.domain.member.entity.MemberEntity;
import com.sparta.springtrello.domain.member.enums.MemberRole;
import com.sparta.springtrello.domain.member.repository.MemberRepository;
import com.sparta.springtrello.domain.notification.service.NotificationService;
import com.sparta.springtrello.domain.user.entity.CustomUserDetails;
import com.sparta.springtrello.domain.user.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListService {

    private final ListRepository listRepository;
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final NotificationService notificationService;

    @Transactional
    public ListResponse createList(Long boardId, CustomUserDetails authUser, ListRequest listRequest) {
        // User 인증
        UserEntity.fromAuthUser(authUser);

        // Board 찾기
        BoardEntity board = findBoardById(boardId);

        // 멤버 여부 확인과 워크스페이스 찾기 (한 번에 처리)
        MemberEntity member = validateMemberInWorkspace(authUser.getId(), board.getWorkspace().getId());

        // 읽기 전용 멤버는 리스트 생성을 할 수 없음
        validatePermission(member);

        // 새로운 리스트의 순서가 기존 리스트들과 겹치는 경우, 그 이후의 리스트 순서를 증가시킴
        reorderListsUpwards(board, listRequest.getListOrder());

        // 리스트 생성
        ListEntity listEntity = new ListEntity(
                listRequest.getTitle(),
                listRequest.getListOrder(),
                board
        );

        // 리스트 저장
        ListEntity savedList = listRepository.save(listEntity);

        // 슬랙 알림 전송
        String message = String.format("%s님이 보드[%s]에 리스트[%s]를 생성했습니다.", authUser.getEmail(), board.getTitle(), listRequest.getTitle());
        notificationService.createNotification(message, "list");


        // ListResponseDto 반환
        return new ListResponse(
                savedList.getId(),
                savedList.getTitle(),
                savedList.getListOrder(),
                savedList.getCreatedAt(),
                savedList.getUpdatedAt()
        );
    }

    @Transactional
    public ListResponse updateList(Long boardId,  Long listId, CustomUserDetails authUser, ListRequest listRequest) {
        // User 인증
        UserEntity.fromAuthUser(authUser);

        // Board 찾기
        BoardEntity board = findBoardById(boardId);

        // 멤버 여부 확인과 워크스페이스 찾기 (한 번에 처리)
        MemberEntity member = validateMemberInWorkspace(authUser.getId(), board.getWorkspace().getId());

        // 읽기 전용 멤버는 리스트 수정을 할 수 없음
        validatePermission(member);

        // ListEntity 찾기
        ListEntity existingList = findListById(listId);

        // 순서가 변경된 경우 재정렬
        if (!existingList.getListOrder().equals(listRequest.getListOrder())) {
            if (listRequest.getListOrder() > existingList.getListOrder()) {
                reorderListsDownwards(board, existingList.getListOrder(), listRequest.getListOrder());
            } else {
                reorderListsUpwards(board, listRequest.getListOrder());
            }
        }

        // 리스트 정보 업데이트
        existingList.update(listRequest.getTitle(), listRequest.getListOrder());

        // 저장
        ListEntity updatedList = listRepository.save(existingList);

        // 슬랙 알림 전송
        String message = String.format("%s님이 보드[%s]의 리스트[%s]를 수정했습니다.", authUser.getEmail(), board.getTitle(), updatedList.getTitle());
        notificationService.createNotification(message, "list");


        // ListResponseDto 반환
        return new ListResponse(
                updatedList.getId(),
                updatedList.getTitle(),
                updatedList.getListOrder(),
                updatedList.getCreatedAt(),
                updatedList.getUpdatedAt()
        );
    }

    @Transactional
    public void deleteList(Long boardId, Long listId, CustomUserDetails authUser) {
        // User 인증
        UserEntity.fromAuthUser(authUser);

        // Board 찾기
        BoardEntity board = findBoardById(boardId);

        // 멤버 여부 확인과 워크스페이스 찾기 (한 번에 처리)
        MemberEntity member = validateMemberInWorkspace(authUser.getId(), board.getWorkspace().getId());

        // 읽기 전용 멤버는 리스트 삭제할 수 없음
        validatePermission(member);

        // ListEntity 찾기
        ListEntity listEntity =  findListById(listId);

        // 리스트 삭제 전 순서 조정 (해당 리스트의 순서 이후 리스트들의 순서를 줄임)
        reorderListsDownwards(board, listEntity.getListOrder(), Integer.MAX_VALUE);

        // 리스트 삭제 (리스트 내의 모든 카드와 관련 데이터도 삭제됨)
        listRepository.delete(listEntity);

        // 슬랙 알림 전송
        String message = String.format("%s님이 보드[%s]의 리스트[%s]를 삭제했습니다.", authUser.getEmail(), board.getTitle(), listEntity.getTitle());
        notificationService.createNotification(message, "list");
    }

    // 리스트 순서 증가 (위로 이동하는 경우)
    private void reorderListsUpwards(BoardEntity board, int newOrder) {
        List<ListEntity> lists = listRepository.findByBoardAndListOrderGreaterThanEqual(board, newOrder);
        lists.forEach(list -> updateListOrder(list, list.getListOrder() + 1));
    }

    // 리스트 순서 감소 (아래로 이동하는 경우)
    private void reorderListsDownwards(BoardEntity board, int oldOrder, int newOrder) {
        List<ListEntity> lists = listRepository.findByBoardAndListOrderBetween(board, oldOrder + 1, newOrder);
        lists.forEach(list -> updateListOrder(list, list.getListOrder() - 1));
    }

    // 리스트 순서 업데이트 공통 로직
    private void updateListOrder(ListEntity list, int newOrder) {
        list.setListOrder(newOrder);
    }

    // 멤버 확인 로직
    private MemberEntity validateMemberInWorkspace(Long userId, Long workspaceId) {
        return memberRepository.findByUserIdAndWorkspaceId(userId, workspaceId)
                .orElseThrow(() -> new CustomException(400, "해당 워크스페이스에 가입된 멤버가 아닙니다."));
    }

    // 보드 찾기 로직
    private BoardEntity findBoardById(Long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(() -> new CustomException(404, "해당 보드를 찾을 수 없습니다."));
    }

    // 리스트 찾기 로직
    private ListEntity findListById(Long listId) {
        return listRepository.findById(listId)
                .orElseThrow(() -> new CustomException(404, "해당 보드를 찾을 수 없습니다."));
    }

    private void validatePermission(MemberEntity member) {
        if (member.getMemberRole() == MemberRole.READ_ONLY) {
            throw new CustomException(403, "읽기 전용 역할을 가진 멤버는 리스트를 생성, 수정, 삭제할 수 없습니다.");
        }
    }
}
