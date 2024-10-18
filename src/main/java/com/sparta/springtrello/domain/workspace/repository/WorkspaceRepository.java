package com.sparta.springtrello.domain.workspace.repository;

import com.sparta.springtrello.domain.workspace.entity.WorkspaceEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkspaceRepository extends JpaRepository<WorkspaceEntity, Long> {

    @EntityGraph(attributePaths = "members")
    List<WorkspaceEntity> findByMembers_UserId(Long userId);
}
