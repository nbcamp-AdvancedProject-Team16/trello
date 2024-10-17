package com.sparta.springtrello.domain.member.repository;

import com.sparta.springtrello.domain.member.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
    List<MemberEntity> findAllByUserId(Long userId);

    // 유저 ID와 워크스페이스 ID로 멤버를 찾는 메서드
    Optional<MemberEntity> findByUserIdAndWorkspaceId(Long id, Long workspaceId);
}
