package com.sparta.springtrello.domain.board.service;

import com.sparta.springtrello.domain.board.dto.request.BoardRequest;
import com.sparta.springtrello.domain.board.dto.response.BoardResponse;
import com.sparta.springtrello.domain.board.entity.BoardEntity;
import com.sparta.springtrello.domain.board.repository.BoardRepository;
import com.sparta.springtrello.domain.card.entity.dto.response.CardResponse;
import com.sparta.springtrello.domain.common.exception.CustomException;
import com.sparta.springtrello.domain.list.dto.response.ListResponse;
import com.sparta.springtrello.domain.member.entity.MemberEntity;
import com.sparta.springtrello.domain.member.enums.MemberRole;
import com.sparta.springtrello.domain.member.repository.MemberRepository;
import com.sparta.springtrello.domain.user.entity.CustomUserDetails;
import com.sparta.springtrello.domain.user.entity.UserEntity;
import com.sparta.springtrello.domain.user.repository.UserRepository;
import com.sparta.springtrello.domain.workspace.entity.WorkspaceEntity;
import com.sparta.springtrello.domain.workspace.repository.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final WorkspaceRepository workspaceRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public BoardResponse createBoard(CustomUserDetails authUser, Long memberId, Long workspaceId, BoardRequest boardRequest) {
        // User 인증
        UserEntity.fromAuthUser(authUser);

        // 멤버 여부
        MemberEntity member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(403, "멤버가 아닙니다."));

        // 읽기 전용 멤버는 보드 생성이 불가능
        validatePermission(member);

        // WorkspaceEntity 찾기
        workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new CustomException(404, "해당 워크스페이스를 찾을 수 없습니다."));

        // 배경색과 배경 이미지 URL 중 하나라도 입력이 없으면 기본값으로 설정
        String backgroundColor = boardRequest.getBackgroundColor();
        String backgroundImageUrl = boardRequest.getBackgroundImageUrl();

        if (backgroundColor == null && backgroundImageUrl == null) {
            // 둘 다 없으면 기본 배경색을 하얀색(#FFFFFF)으로 설정
            backgroundColor = "#FFFFFF";
        }

        // BoardEntity 생성
        BoardEntity board = new BoardEntity(
                boardRequest.getTitle(),
                backgroundColor,
                backgroundImageUrl
        );

        // 제목이 비어 있으면 예외 처리
        if (boardRequest.getTitle() == null || boardRequest.getTitle().isEmpty()) {
            throw new CustomException(400, "제목이 비어 있습니다.");
        }

        // 저장
        BoardEntity savedBoard = boardRepository.save(board);

        // BoardResponse 반환
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
    public BoardResponse updateBoard(Long memberId, Long boardId, Long workspaceId, CustomUserDetails authUser, BoardRequest boardRequest) {
        // User 인증
        UserEntity.fromAuthUser(authUser);

        // 로그인 여부 확인
        MemberEntity member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(403, "멤버가 아닙니다."));

        // 읽기 전용 멤버는 보드를 수정할 수 없음
        validatePermission(member);

        // WorkspaceEntity 찾기
        workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new CustomException(404, "해당 워크스페이스를 찾을 수 없습니다."));

        // BoardEntity 찾기햐
        BoardEntity existingBoard = boardRepository.findById(boardId)
                .orElseThrow(() -> new CustomException(404, "해당 보드를 찾을 수 없습니다."));

        // 배경색과 이미지 업데이트 처리
        String updatedTitle = boardRequest.getTitle();
        String updatedBackgroundColor = boardRequest.getBackgroundColor() != null ? boardRequest.getBackgroundColor() : existingBoard.getBackgroundColor();
        String updatedBackgroundImageUrl = boardRequest.getBackgroundImageUrl() != null ? boardRequest.getBackgroundImageUrl() : existingBoard.getBackgroundImageUrl();

        existingBoard.update(updatedTitle, updatedBackgroundColor, updatedBackgroundImageUrl);

        // 제목이 비어 있으면 예외 처리
        if (boardRequest.getTitle() == null || boardRequest.getTitle().isEmpty()) {
            throw new CustomException(400, "보드 제목은 필수 항목입니다.");
        }

        // 저장
        BoardEntity savedBoard = boardRepository.save(existingBoard);

        // BoardResponse 반환
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
    public void deleteBoard(Long memberId, Long boardId, Long workspaceId, CustomUserDetails authUser) {
        // User 인증
        UserEntity.fromAuthUser(authUser);

        // 로그인 여부 확인
        MemberEntity member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(403, "멤버가 아닙니다."));

        // 읽기 전용 멤버는 보드를 삭제할 수 없음
        validatePermission(member);

        // WorkspaceEntity 찾기
        workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new CustomException(404, "해당 워크스페이스를 찾을 수 없습니다."));

        // BoardEntity 찾기
        BoardEntity board = boardRepository.findById(boardId)
                .orElseThrow(() -> new CustomException(404, "해당 보드를 찾을 수 없습니다."));

        // board 삭제 (연관된 리스트와 카드도 함께 삭제됨)
        boardRepository.delete(board);
    }

    @Transactional
    public BoardResponse getBoard(Long memberId, Long boardId, Long workspaceId, CustomUserDetails authUser) {
        // User 인증
        UserEntity.fromAuthUser(authUser);

        // 로그인 여부 확인
        MemberEntity member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(403, "멤버가 아닙니다."));

        // WorkspaceEntity 찾기
        workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new CustomException(404, "해당 워크스페이스를 찾을 수 없습니다."));

        // 보드 존재 여부 확인
        BoardEntity board = boardRepository.findById(boardId)
                .orElseThrow(() -> new CustomException(404, "해당 보드를 찾을 수 없습니다."));

        // 리스트 및 카드 데이터도 함께 반환
        List<ListResponse> lists = board.getLists().stream()
                .map(list -> new ListResponse(
                        list.getId(),
                        list.getTitle(),
                        list.getCards().stream()
                                .map(card -> new CardResponse(card.getId(), card.getTitle(), card.getDescription(), card.getDueDate()))
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

    private void validatePermission(MemberEntity member) {
        if (member.getMemberRole() == MemberRole.READ_ONLY) {
            throw new CustomException(403, "읽기 전용 역할을 가진 멤버는 보드를 생성할 수 없습니다.");
        }
    }
}
