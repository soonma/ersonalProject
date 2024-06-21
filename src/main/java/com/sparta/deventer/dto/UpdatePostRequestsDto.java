package com.sparta.deventer.dto;

import com.sparta.deventer.entity.Category;
import lombok.Getter;

@Getter
public class UpdatePostRequestsDto {
    private String title;
    private String content;
    private Category category;
}
