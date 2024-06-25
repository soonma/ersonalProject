package com.sparta.deventer.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChangePostCategoryRequestDto {

    @NotBlank(message = "카테고리 주제를 입력해야 합니다.")
    private String categoryTopic;
}