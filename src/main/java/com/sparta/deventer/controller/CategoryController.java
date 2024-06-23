package com.sparta.deventer.controller;

import com.sparta.deventer.dto.CategoryRequestDto;
import com.sparta.deventer.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("/categories")
    public ResponseEntity<CategoryResponseDto> createCategory(
            @Valid @RequestBody CategoryRequestDto requestDto) {
        return ResponseEntity.ok().body(categoryService.createCategory(requestDto));
    }
}
