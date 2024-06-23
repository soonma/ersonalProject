package com.sparta.deventer.dto;

import lombok.Getter;
@Getter
public class PostRequestDto {

    private String title;
    private String content;
    private String categoryTopic;





    public PostRequestDto(String title, String content, String categoryTopic) {
        this.title = title;
        this.content = content;
        this.categoryTopic = categoryTopic;
    }



}
