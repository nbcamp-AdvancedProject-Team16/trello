package com.sparta.springtrello.domain.comment.dto;

import com.sparta.springtrello.domain.comment.entity.CommentEntity;
import lombok.Getter;

@Getter
public class CommentCardResponse {
    private String text;

    public CommentCardResponse(CommentEntity commentEntity){
        this.text = commentEntity.getText();
    }
}