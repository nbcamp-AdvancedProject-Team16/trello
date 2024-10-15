package com.sparta.springtrello.domain.list.service;

import com.sparta.springtrello.domain.board.entity.BoardEntity;
import com.sparta.springtrello.domain.board.repository.BoardRepository;
import com.sparta.springtrello.domain.common.exception.CustomException;
import com.sparta.springtrello.domain.list.dto.request.ListRequest;
import com.sparta.springtrello.domain.list.dto.response.ListResponse;
import com.sparta.springtrello.domain.list.entity.ListEntity;
import com.sparta.springtrello.domain.list.repository.ListRepository;
import com.sparta.springtrello.domain.member.entity.MemberEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ListService {

    private final ListRepository listRepository;
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public ListResponse createList(Long boardId, CustomUserDetails authUser, ListRequest listRequest) {
        // 로그인 여부 확인
        MemberEntity member = memberRepository.findByUserIdAndWorkspaceId(authUser.getId(), boardId)
                .orElseThrow(() -> new CustomException(401, "로그인이 필요합니다."));

        // 읽기 전용 멤버는 리스트를 생성할 수 없음
        if (member.isReadOnly()) {
            throw new CustomException(403, "읽기 전용 역할을 가진 멤버는 리스트를 생성할 수 없습니다.");
        }

        // BoardEntity 찾기
        BoardEntity board = boardRepository.findById(boardId)
                .orElseThrow(() -> new CustomException(404, "해당 보드를 찾을 수 없습니다."));

        // 리스트 생성
        ListEntity listEntity = new ListEntity(
                listRequest.getTitle(),
                listRequest.getListOrder(),
                board
        );

        // 리스트 저장
        ListEntity savedList = listRepository.save(listEntity);

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
    public ListResponse updateList(Long listId, CustomUserDetails authUser, ListRequest listRequest) {
        // 로그인 여부 확인
        MemberEntity member = memberRepository.findByUserIdAndWorkspaceId(authUser.getId(), boardId)
                .orElseThrow(() -> new CustomException(401, "로그인이 필요합니다."));

        // 읽기 전용 멤버는 리스트를 수정할 수 없음
        if (member.isReadOnly()) {
            throw new CustomException(403, "읽기 전용 역할을 가진 멤버는 리스트를 수정할 수 없습니다.");
        }

        // ListEntity 찾기
        ListEntity existingList = listRepository.findById(listId)
                .orElseThrow(() -> new CustomException(404, "해당 리스트를 찾을 수 없습니다."));

        // 새로운 순서로 이동하는 경우
        if (!existingList.getListOrder().equals(listRequest.getListOrder())) {
            reorderLists(existingList.getBoard(), existingList.getListOrder(), listRequest.getListOrder());
        }

        // 리스트 정보 업데이트
        existingList.update(listRequest.getTitle(), listRequest.getListOrder());

        // 저장
        ListEntity updatedList = listRepository.save(existingList);

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
    public void deleteList(Long listId, CustomUserDetails authUser) {
        // 로그인 여부 확인
        MemberEntity member = memberRepository.findByUserIdAndWorkspaceId(authUser.getId(), boardId)
                .orElseThrow(() -> new CustomException(401, "로그인이 필요합니다."));

        // 읽기 전용 멤버는 리스트를 삭제할 수 없음
        if (member.isReadOnly()) {
            throw new CustomException(403, "읽기 전용 역할을 가진 멤버는 리스트를 삭제할 수 없습니다.");
        }

        // ListEntity 찾기
        ListEntity listEntity = listRepository.findById(listId)
                .orElseThrow(() -> new CustomException(404, "해당 리스트를 찾을 수 없습니다."));

        // 리스트 삭제 (리스트 내의 모든 카드와 관련 데이터도 삭제됨)
        listRepository.delete(listEntity);
    }

    // 리스트 순서 재정렬 로직
    private void reorderLists(BoardEntity board, int oldOrder, int newOrder) {
        // 이동할 순서가 더 큰 경우 (리스트가 하위로 이동하는 경우)
        if (newOrder > oldOrder) {
            // oldOrder 보다 크고 newOrder 이하인 리스트들의 순서를 하나씩 감소시킴
            listRepository.findByBoardAndListOrderBetween(board, oldOrder + 1, newOrder)
                    .forEach(list -> list.setListOrder(list.getListOrder() - 1));
        }
        // 이동할 순서가 더 작은 경우 (리스트가 상위로 이동하는 경우)
        else if (newOrder < oldOrder) {
            // newOrder 보다 크거나 같고 oldOrder 보다 작은 리스트들의 순서를 하나씩 증가시킴
            listRepository.findByBoardAndListOrderBetween(board, newOrder, oldOrder - 1)
                    .forEach(list -> list.setListOrder(list.getListOrder() + 1));
        }
    }
}
