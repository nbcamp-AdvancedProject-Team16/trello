package com.sparta.springtrello.domain.list.repository;

import com.sparta.springtrello.domain.board.entity.BoardEntity;
import com.sparta.springtrello.domain.list.entity.ListEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ListRepository extends JpaRepository<ListEntity, Long> {
    List<ListEntity> findByBoardAndListOrderBetween(BoardEntity board, int startOrder, int endOrder);

    List<ListEntity> findByBoardAndListOrderGreaterThanEqual(BoardEntity board, int newOrder);
}
