package com.sparta.springtrello.domain.card.dto;

import com.sparta.springtrello.domain.attachment.entity.AttachmentEntity;
import com.sparta.springtrello.domain.card.entity.CardEntity;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
public class CardResponse<T> {
    private String title;
    private String description;
    private LocalDate dueDate;
    private String message;

    private List<AttachmentEntity> attachmentEntityList;
    private T data; //이거 어디에 넣어

    public CardResponse(CardEntity card){
        this.title = card.getTitle();
        this.description = card.getDescription();
        this.dueDate = card.getDueDate();
    }

    public void message(String message) {
        this.message = message;
    }


}
