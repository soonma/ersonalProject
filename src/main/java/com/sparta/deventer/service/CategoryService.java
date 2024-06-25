package com.sparta.deventer.service;

import com.sparta.deventer.dto.CategoryRequestDto;
import com.sparta.deventer.dto.CategoryResponseDto;
import com.sparta.deventer.entity.Category;
import com.sparta.deventer.enums.NotFoundEntity;
import com.sparta.deventer.exception.CategoryDuplicateException;
import com.sparta.deventer.exception.EntityNotFoundException;
import com.sparta.deventer.repository.CategoryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    /**
     * 카테고리를 생성합니다.
     *
     * @param categoryRequestDto 카테고리 요청 DTO
     * @return 생성된 카테고리 정보
     */
    public CategoryResponseDto createCategory(CategoryRequestDto categoryRequestDto) {
        validateDuplicateCategoryTopic(categoryRequestDto.getTopic());

        Category category = new Category(categoryRequestDto.getTopic());
        categoryRepository.save(category);
        return new CategoryResponseDto(category);
    }

    /**
     * 카테고리 목록을 조회합니다.
     *
     * @return 카테고리 목록
     */
    @Transactional(readOnly = true)
    public List<CategoryResponseDto> getAllCategories() {
        return categoryRepository.findAll().stream().map(CategoryResponseDto::new).toList();
    }

    /**
     * 카테고리 이름을 변경합니다.
     *
     * @param categoryId         변경할 카테고리 고유번호
     * @param categoryRequestDto 변경할 카테고리 요청 DTO
     * @return 변경된 카테고리 정보
     */
    @Transactional
    public CategoryResponseDto updateCategory(
        Long categoryId,
        CategoryRequestDto categoryRequestDto) {

        Category category = getCategoryByIdOrThrow(categoryId);
        validateDuplicateCategoryTopic(categoryRequestDto.getTopic());

        category.updateTopic(categoryRequestDto.getTopic());

        return new CategoryResponseDto(category);
    }

    /**
     * 카테고리를 삭제합니다.
     *
     * @param categoryId 삭제할 카테고리 고유번호
     * @return 삭제 완료 메시지
     */
    @Transactional
    public String deleteCategory(Long categoryId) {
        Category category = getCategoryByIdOrThrow(categoryId);
        categoryRepository.delete(category);
        return "[" + category.getTopic() + "] 카테고리가 삭제되었습니다.";
    }

    /**
     * 카테고리 토픽 중복 확인 로직
     *
     * @param topic 확인할 카테고리 토픽
     */
    private void validateDuplicateCategoryTopic(String topic) {
        if (categoryRepository.existsByTopic(topic)) {
            throw new CategoryDuplicateException("이미 존재하는 카테고리입니다.");
        }
    }

    /**
     * 카테고리 객체를 ID로 찾고, 찾을 수 없으면 예외를 던집니다.
     *
     * @param categoryId 찾을 카테고리 ID
     * @return 찾은 카테고리 객체
     */
    private Category getCategoryByIdOrThrow(Long categoryId) {
        return categoryRepository.findById(categoryId)
            .orElseThrow(() -> new EntityNotFoundException(NotFoundEntity.CATEGORY_NOT_FOUND));
    }
}