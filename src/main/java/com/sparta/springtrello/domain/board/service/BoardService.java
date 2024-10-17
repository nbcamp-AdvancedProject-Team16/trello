package com.sparta.springtrello.domain.board.service;

import com.sparta.springtrello.domain.board.dto.request.BoardRequest;
import com.sparta.springtrello.domain.board.dto.response.BoardResponse;
import com.sparta.springtrello.domain.board.entity.BoardEntity;
import com.sparta.springtrello.domain.board.repository.BoardRepository;
import com.sparta.springtrello.domain.card.dto.CardResponse;
import com.sparta.springtrello.domain.common.exception.CustomException;
import com.sparta.springtrello.domain.list.dto.response.ListResponse;
import com.sparta.springtrello.domain.member.entity.MemberEntity;
import com.sparta.springtrello.domain.member.enums.MemberRole;
import com.sparta.springtrello.domain.member.repository.MemberRepository;
import com.sparta.springtrello.domain.user.entity.CustomUserDetails;
import com.sparta.springtrello.domain.user.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public BoardResponse createBoard(CustomUserDetails authUser, Long workspaceId, BoardRequest boardRequest) {
        // User 인증
        UserEntity.fromAuthUser(authUser);

        // 멤버 여부 확인
        MemberEntity member = validateMemberInWorkspace(authUser.getId(), workspaceId);

        // 읽기 전용 멤버는 보드를 생성할 수 없음
        validatePermission(member);

        // 배경색 설정 (없으면 기본값)
        String backgroundColor = boardRequest.getBackgroundColor() != null ? boardRequest.getBackgroundColor() : "#FFFFFF";

        // 보드 생성
        BoardEntity board = new BoardEntity(
                boardRequest.getTitle(),
                backgroundColor,
                boardRequest.getBackgroundImageUrl(),
                member.getWorkspace()
        );

        // 보드 저장
        BoardEntity savedBoard = boardRepository.save(board);

        // 결과 반환
        return new BoardResponse(
                savedBoard.getId(),
                savedBoard.getTitle(),
                savedBoard.getBackgroundColor(),
                savedBoard.getBackgroundImageUrl(),
                savedBoard.getCreatedAt(),
                savedBoard.getUpdatedAt()
        );
    }

    @Transactional
    public BoardResponse updateBoard(Long boardId, Long workspaceId, CustomUserDetails authUser, BoardRequest boardRequest) {
        // User 인증
        UserEntity.fromAuthUser(authUser);

        // 워크스페이스 멤버 여부 확인
        MemberEntity member = validateMemberInWorkspace(authUser.getId(), workspaceId);

        // 읽기 전용 멤버는 보드를 수정할 수 없음
        validatePermission(member);

        // BoardEntity 찾기
        BoardEntity existingBoard = findBoardById(boardId);

        existingBoard.update(boardRequest);

        // 결과 반환
        return new BoardResponse(
                existingBoard.getId(),
                existingBoard.getTitle(),
                existingBoard.getBackgroundColor(),
                existingBoard.getBackgroundImageUrl(),
                existingBoard.getCreatedAt(),
                existingBoard.getUpdatedAt()
        );
    }

    @Transactional
    public void deleteBoard(Long boardId, Long workspaceId, CustomUserDetails authUser) {
        // User 인증
        UserEntity.fromAuthUser(authUser);

        // 멤버 및 권한 확인
        MemberEntity member = validateMemberInWorkspace(authUser.getId(), workspaceId);

        // 읽기 전용 멤버는 보드를 삭제할 수 없음
        validatePermission(member);

        // BoardEntity 찾기
        BoardEntity board = findBoardById(boardId);

        // board 삭제 (연관된 리스트와 카드도 함께 삭제됨)
        boardRepository.delete(board);
    }

    @Transactional
    public BoardResponse getBoard(Long boardId, Long workspaceId, CustomUserDetails authUser) {
        // User 인증
        UserEntity.fromAuthUser(authUser);

        // 워크스페이스 멤버 여부 확인
        validateMemberInWorkspace(authUser.getId(), workspaceId);

        // 보드 존재 여부 확인
        BoardEntity board = findBoardById(boardId);

        // 리스트 및 카드 데이터도 함께 반환
        List<ListResponse> lists = board.getLists().stream()
                .map(list -> new ListResponse(
                        list.getId(),
                        list.getTitle(),
                        list.getListOrder(),
                        list.getCreatedAt(),
                        list.getUpdatedAt(),
                        list.getCards().stream()
                                .map(card -> new CardResponse(card.getTitle(), card.getDescription(), card.getDueDate()))
                                .collect(Collectors.toList())
                ))
                .collect(Collectors.toList());

        // 결과 반환
        return new BoardResponse(
                board.getId(),
                board.getTitle(),
                board.getBackgroundColor(),
                board.getBackgroundImageUrl(),
                board.getCreatedAt(),
                board.getUpdatedAt(),
                lists // 리스트와 카드 데이터 포함
        );
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

    // 읽기 전용 멤버인지 확인 로직
    private void validatePermission(MemberEntity member) {
        if (member.getMemberRole() == MemberRole.READ_ONLY) {
            throw new CustomException(403, "읽기 전용 역할을 가진 멤버는 보드를 생성, 수정, 삭제할 수 없습니다.");
        }
    }
}