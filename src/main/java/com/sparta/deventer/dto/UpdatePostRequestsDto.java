package com.sparta.deventer.dto;

import com.sparta.deventer.entity.Category;
import com.sparta.deventer.entity.Post;
import lombok.Getter;

@Getter
public class UpdatePostRequestsDto {
    private String title;
    private String content;



    public UpdatePostRequestsDto(String title, String content) {
        this.title = title;
        this.content = content;
    }
}

