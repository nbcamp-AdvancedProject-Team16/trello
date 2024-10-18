package com.sparta.springtrello.domain.notification.service;

import com.sparta.springtrello.domain.notification.entity.NotificationSettingEntity;
import com.sparta.springtrello.domain.notification.repository.NotificationSettingRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class NotificationSettingInitializer implements CommandLineRunner {

    private final NotificationSettingRepository notificationSettingRepository;

    public NotificationSettingInitializer(NotificationSettingRepository notificationSettingRepository) {
        this.notificationSettingRepository = notificationSettingRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (notificationSettingRepository.count() == 0) { // 데이터가 없을 경우만 추가
            NotificationSettingEntity defaultSetting = new NotificationSettingEntity("slack", true);
            notificationSettingRepository.save(defaultSetting);
            System.out.println("Default notification setting added: " + defaultSetting);
        }
    }
}