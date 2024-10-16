package com.sparta.springtrello.domain.activity.service;

import com.sparta.springtrello.domain.activity.entity.ActivityLogEntity;
import com.sparta.springtrello.domain.activity.repository.ActivityLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ActivityLogService {

    private final ActivityLogRepository activityLogRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveLog(String content) {
        activityLogRepository.save(new ActivityLogEntity(content));
    }
}
