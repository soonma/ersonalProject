package com.sparta.deventer.dto;

import com.sparta.deventer.entity.Category;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CategoryResponseDto {

    private Long categoryId;
    private String topic;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public CategoryResponseDto(Category category) {
        this.categoryId = category.getId();
        this.topic = category.getTopic();
        this.createdAt = category.getCreatedAt();
        this.updatedAt = category.getUpdateAt();
    }
}
