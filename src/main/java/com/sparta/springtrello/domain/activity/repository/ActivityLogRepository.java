package com.sparta.springtrello.domain.activity.repository;

import com.sparta.springtrello.domain.activity.entity.ActivityLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityLogRepository extends JpaRepository<ActivityLogEntity,Long> {
}
