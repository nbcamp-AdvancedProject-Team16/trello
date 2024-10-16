package com.sparta.springtrello.domain.comment.dto;

import com.sparta.springtrello.domain.comment.entity.CommentEntity;
import lombok.Getter;

@Getter
public class CommentResponse {

    private String text;
    private String message;


    public CommentResponse(CommentEntity comment){
        this.text = comment.getText();
    }

    public void message(String message) {
        this.message = message;
    }
}
