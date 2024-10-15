package com.sparta.springtrello.domain.list.repository;

import com.sparta.springtrello.domain.board.entity.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ListRepository extends JpaRepository<BoardEntity, Long> {
}
