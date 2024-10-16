package com.sparta.springtrello.domain.notification.repository;

import com.sparta.springtrello.domain.notification.entity.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {
}
