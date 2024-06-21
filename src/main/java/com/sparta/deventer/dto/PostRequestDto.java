package com.sparta.deventer.dto;

import lombok.Getter;
@Getter
public class PostRequestDto {

    private String title;
    private String content;
    private Long categoryId;

    public PostRequestDto(String title, String content, Long categoryId) {
        this.title = title;
        this.content = content;
        this.categoryId = categoryId;
    }



}
