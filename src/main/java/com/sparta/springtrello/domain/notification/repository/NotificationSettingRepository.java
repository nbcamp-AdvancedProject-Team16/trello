package com.sparta.springtrello.domain.notification.repository;

import com.sparta.springtrello.domain.notification.entity.NotificationSettingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationSettingRepository extends JpaRepository<NotificationSettingEntity, Long> {
}
