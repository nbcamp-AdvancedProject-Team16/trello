package com.sparta.springtrello.domain.notification.service;

import com.sparta.springtrello.domain.notification.config.AppConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

@Service
@RequiredArgsConstructor
public class SlackService {

    @Value("${slack.webhook.url}")
    private String webhookUrl;

    private final RestTemplate restTemplate;

    public void sendSlackNotification(String message) {
        // UTF-8로 인코딩된 JSON 페이로드 생성
        String payload = String.format("{\"text\": \"%s\"}", message);

        // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // HTTP 엔티티 생성
        HttpEntity<String> entity = new HttpEntity<>(payload, headers);

        // 슬랙으로 메시지 전송
        restTemplate.postForObject(webhookUrl, entity, String.class);
    }
}