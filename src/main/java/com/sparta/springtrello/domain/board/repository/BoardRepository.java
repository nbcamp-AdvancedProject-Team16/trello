package com.sparta.springtrello.domain.board.repository;

import com.sparta.springtrello.domain.list.entity.ListEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<ListEntity, Long> {
}
