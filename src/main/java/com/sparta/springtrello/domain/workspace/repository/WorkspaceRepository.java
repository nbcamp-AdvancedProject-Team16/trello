package com.sparta.springtrello.domain.workspace.repository;

import com.sparta.springtrello.domain.workspace.entity.WorkspaceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkspaceRepository extends JpaRepository<WorkspaceEntity, Long> {
}
