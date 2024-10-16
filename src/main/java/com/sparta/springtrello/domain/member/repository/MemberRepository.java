package com.sparta.springtrello.domain.member.repository;

import com.sparta.springtrello.domain.member.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
    List<MemberEntity> findAllByUserId(Long userId);
}
