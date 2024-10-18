package com.sparta.springtrello.domain.activity.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "activity_log")
public class ActivityLogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    private LocalDateTime createdAt;

    public ActivityLogEntity(String content) {
        this.content = content;
        this.createdAt = LocalDateTime.now();
    }

}
