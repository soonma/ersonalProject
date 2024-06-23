package com.sparta.deventer.service;

import com.sparta.deventer.controller.CategoryResponseDto;
import com.sparta.deventer.dto.CategoryRequestDto;
import com.sparta.deventer.entity.Category;
import com.sparta.deventer.exception.CategoryDuplicateException;
import com.sparta.deventer.exception.CategoryNotFoundException;
import com.sparta.deventer.repository.CategoryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryResponseDto createCategory(CategoryRequestDto requestDto) {

        if (categoryRepository.existsByTopic(requestDto.getTopic())) {
            throw new CategoryDuplicateException("이미 존재하는 카테고리입니다.");
        }

        Category category = new Category(requestDto.getTopic());

        categoryRepository.save(category);

        return makeResponseDto(category);
    }

    @Transactional(readOnly = true)
    public List<CategoryResponseDto> getAllCategory() {
        return categoryRepository.findAll().stream()
                .map(category -> new CategoryResponseDto(
                        category.getId(),
                        category.getTopic(),
                        category.getCreatedAt(),
                        category.getUpdateAt()))
                .toList();
    }

    @Transactional
    public CategoryResponseDto changeCategory(Long categoryId, CategoryRequestDto requestDto) {
        Category category = getCategoryById(categoryId);

        category.updateTopic(requestDto.getTopic());

        return makeResponseDto(category);
    }

    private Category getCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("카테고리를 찾지 못했습니다."));
    }

    private CategoryResponseDto makeResponseDto(Category category) {
        return new CategoryResponseDto(
                category.getId(),
                category.getTopic(),
                category.getCreatedAt(),
                category.getUpdateAt()
        );
    }
}
