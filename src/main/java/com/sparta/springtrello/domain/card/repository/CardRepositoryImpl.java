package com.sparta.springtrello.domain.card.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.springtrello.domain.assignee.entity.QAssigneeEntity;
import com.sparta.springtrello.domain.card.dto.CardSearchResponse;
import com.sparta.springtrello.domain.card.entity.QCardEntity;
import com.sparta.springtrello.domain.member.entity.QMemberEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CardRepositoryImpl implements CardRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    QCardEntity card = QCardEntity.cardEntity;
    QAssigneeEntity assigneeEntity = QAssigneeEntity.assigneeEntity;

    @Override
    public Page<CardSearchResponse> searchCards(
            Pageable pageable, String title, String description, LocalDate dueDate, String assignee) {

        List<CardSearchResponse> results = queryFactory
                .select(Projections.constructor(
                        CardSearchResponse.class,
                        card.id,
                        card.title
                ))
                .from(card)
                .leftJoin(card.assigneeEntityList, assigneeEntity)
                .where(
                        titleContains(title),
                        descriptionContains(description),
                        dueDateEq(dueDate),
                        assigneeNameContains(assignee)
                )
                .orderBy(card.id.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = Optional.ofNullable(
                queryFactory
                        .select(card.count())
                        .from(card)
                        .where(
                                titleContains(title),
                                descriptionContains(description),
                                dueDateEq(dueDate),
                                assigneeNameContains(assignee)
                        )
                        .fetchOne()
        ).orElse(0L);

        long total = count;  // 안전하게 unboxing

        return new PageImpl<>(results, pageable, total);
    }

    private BooleanExpression titleContains(String title) {
        return title != null ? QCardEntity.cardEntity.title.containsIgnoreCase(title) : null;
    }

    private BooleanExpression descriptionContains(String description) {
        return description != null ? QCardEntity.cardEntity.description.containsIgnoreCase(description) : null;
    }

    private BooleanExpression dueDateEq(LocalDate dueDate) {
       return dueDate != null ? QCardEntity.cardEntity.dueDate.eq(dueDate) : null;
    }

    private BooleanExpression assigneeNameContains(String assignee) {
        return assignee != null
                ? QMemberEntity.memberEntity
                : null;
    }
}



