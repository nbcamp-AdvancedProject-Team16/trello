package com.sparta.springtrello.domain.card.dto;

import com.sparta.springtrello.domain.assignee.dto.AssigneeResponse;
import com.sparta.springtrello.domain.assignee.entity.AssigneeEntity;
import com.sparta.springtrello.domain.card.entity.CardEntity;
import com.sparta.springtrello.domain.comment.dto.CommentCardResponse;
import com.sparta.springtrello.domain.comment.entity.CommentEntity;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CardResponse{
    private String title;
    private String description;
    private LocalDate dueDate;
    private String message;
    private List<AssigneeResponse> assigneeResponseList;
    private List<CommentCardResponse> commentResponseList;

    public CardResponse(String title,String description, LocalDate dueDate){
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
    }

    public CardResponse(CardEntity card, List<AssigneeEntity> assigneeEntityList, String message, List<CommentEntity> commentEntityList) {
        this.title = card.getTitle();
        this.description = card.getDescription();
        this.dueDate = card.getDueDate();
        this.message = message;
        this.assigneeResponseList = assigneeEntityList.stream()
                .map(assignee -> new AssigneeResponse(assignee, message))
                .toList();
        this.commentResponseList = commentEntityList.stream()
                .map(CommentCardResponse::new)
                .toList();
    }

    public void message(String message) {
        this.message = message;
    }
}