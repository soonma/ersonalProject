package com.sparta.deventer.dto;

import lombok.Getter;
@Getter
public class CreatePostRequestDto {

    private String title;
    private String content;
    private Long categoryId;

    public CreatePostRequestDto(String title, String content, Long categoryId) {
        this.title = title;
        this.content = content;
        this.categoryId = categoryId;
    }



}
