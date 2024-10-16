package com.sparta.springtrello.domain.card.dto;

import com.sparta.springtrello.domain.assignee.dto.AssigneeResponse;
import com.sparta.springtrello.domain.assignee.entity.AssigneeEntity;
import com.sparta.springtrello.domain.attachment.entity.AttachmentEntity;
import com.sparta.springtrello.domain.card.entity.CardEntity;
import com.sparta.springtrello.domain.comment.dto.CommentCardResponse;
import com.sparta.springtrello.domain.comment.dto.CommentResponse;
import com.sparta.springtrello.domain.comment.entity.CommentEntity;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
public class CardResponse<T> {
    private String title;
    private String description;
    private LocalDate dueDate;
    private String message;

    private List<AssigneeResponse> assigneeResponseList;
    private List<CommentCardResponse> commentResponseList;
    private T data;

    public CardResponse(CardEntity card){
        this.title = card.getTitle();
        this.description = card.getDescription();
        this.dueDate = card.getDueDate();
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
                .map(comment -> new CommentCardResponse(comment))
                .toList();
    }

    public void message(String message) {
        this.message = message;
    }


}
