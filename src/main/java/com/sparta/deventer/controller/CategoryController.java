package com.sparta.deventer.controller;

import com.sparta.deventer.dto.CategoryRequestDto;
import com.sparta.deventer.dto.CategoryResponseDto;
import com.sparta.deventer.service.CategoryService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    /**
     * 관리자 권한으로 카테고리를 생성합니다.
     *
     * @param categoryRequestDto 카테고리 요청 DTO
     * @return 카테고리 정보
     */
    @PostMapping("/categories")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<CategoryResponseDto> createCategory(
        @Valid @RequestBody CategoryRequestDto categoryRequestDto) {

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(categoryService.createCategory(categoryRequestDto));
    }

    /**
     * 카테고리 목록을 조회합니다.
     *
     * @return 카테고리 목록
     */
    @GetMapping("/categories")
    public ResponseEntity<List<CategoryResponseDto>> getAllCategories() {
        return ResponseEntity.ok().body(categoryService.getAllCategories());
    }

    /**
     * 관리자 권한으로 카테고리 이름을 변경합니다.
     *
     * @param categoryId         변경할 카테고리 고유번호
     * @param categoryRequestDto 변경할 카테고리 요청 DTO
     * @return 수정된 카테고리 정보
     */
    @PutMapping("/categories/{categoryId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<CategoryResponseDto> updateCategory(
        @PathVariable Long categoryId,
        @Valid @RequestBody CategoryRequestDto categoryRequestDto) {

        return ResponseEntity.ok()
            .body(categoryService.updateCategory(categoryId, categoryRequestDto));
    }

    /**
     * 관리자 권한으로 카테고리를 삭제합니다.
     *
     * @param categoryId 삭제할 카테고리 고유번호
     * @return 삭제 완료 메세지
     */
    @DeleteMapping("/categories/{categoryId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> deleteCategory(@PathVariable Long categoryId) {
        return ResponseEntity.ok().body(categoryService.deleteCategory(categoryId));
    }
}
