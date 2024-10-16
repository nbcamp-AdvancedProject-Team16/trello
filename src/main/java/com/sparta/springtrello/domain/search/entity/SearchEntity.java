package com.sparta.springtrello.domain.search.entity;

import com.sparta.springtrello.domain.common.entity.Timestamped;
import com.sparta.springtrello.domain.member.entity.MemberEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
public class SearchEntity extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "search_title")
    private String title;

    @Column(name = "search_dueDate")
    private LocalDate DueDate;

    @Column(name = "search_createdBy")
    private String createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private MemberEntity member;

    public SearchEntity(String title, LocalDate dueDate, String createdBy, MemberEntity member) {
        this.title = title;
        DueDate = dueDate;
        this.createdBy = createdBy;
        this.member = member;
    }
}
