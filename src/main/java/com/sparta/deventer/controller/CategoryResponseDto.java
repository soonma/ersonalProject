package com.sparta.deventer.controller;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CategoryResponseDto {

    private Long id;
    private String topic;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
