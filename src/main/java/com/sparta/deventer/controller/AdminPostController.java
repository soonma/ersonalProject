package com.sparta.deventer.controller;

import com.sparta.deventer.dto.ChangePostCategoryRequestDto;
import com.sparta.deventer.dto.CreatePostRequestDto;
import com.sparta.deventer.dto.PostResponseDto;
import com.sparta.deventer.dto.UpdatePostRequestDto;
import com.sparta.deventer.security.UserDetailsImpl;
import com.sparta.deventer.service.AdminPostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/posts")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminPostController {

    private final AdminPostService adminPostService;

    /**
     * 공지글을 생성합니다.
     *
     * @param createPostRequestDto 게시물 생성 요청 DTO
     * @param userDetails          현재 인증된 사용자 정보
     * @return 생성된 게시글 응답 DTO
     */
    @PostMapping("/notice")
    public ResponseEntity<PostResponseDto> createNotice(
        @Valid @RequestBody CreatePostRequestDto createPostRequestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {

        PostResponseDto postResponseDto = adminPostService.createNotice(
            createPostRequestDto, userDetails.getUser());
        return ResponseEntity.status(HttpStatus.CREATED).body(postResponseDto);
    }

    /**
     * 공지글을 수정합니다.
     *
     * @param postId               수정할 공지글 ID
     * @param updatePostRequestDto 게시물 수정 요청 DTO
     * @return 수정된 게시물 응답 DTO
     */
    @PutMapping("/notice/{postId}")
    public ResponseEntity<PostResponseDto> updateNotice(
        @PathVariable Long postId,
        @Valid @RequestBody UpdatePostRequestDto updatePostRequestDto) {

        PostResponseDto postResponseDto = adminPostService.updateNotice(postId,
            updatePostRequestDto);
        return ResponseEntity.ok(postResponseDto);
    }

    /**
     * 관리자 권한으로 게시물을 수정합니다.
     *
     * @param postId               수정할 게시물 ID
     * @param updatePostRequestDto 게시물 수정 요청 DTO
     * @return 수정된 게시물 응답 DTO
     */
    @PutMapping("/{postId}")
    public ResponseEntity<PostResponseDto> updatePost(
        @PathVariable Long postId,
        @Valid @RequestBody UpdatePostRequestDto updatePostRequestDto) {

        PostResponseDto postResponseDto = adminPostService.updatePost(postId, updatePostRequestDto);
        return ResponseEntity.ok(postResponseDto);
    }

    /**
     * 관리자 권한으로 게시물을 삭제합니다.
     *
     * @param postId 삭제할 게시물 ID
     * @return 삭제 완료 메시지
     */
    @DeleteMapping("/{postId}")
    public ResponseEntity<String> deletePost(@PathVariable Long postId) {

        adminPostService.deletePost(postId);
        return ResponseEntity.ok("관리자 권한으로 게시물을 삭제했습니다.");
    }

    /**
     * 게시글을 카테고리로 이동합니다.
     *
     * @param postId                       이동할 게시물 ID
     * @param changePostCategoryRequestDto 카테고리 이동 요청 DTO
     * @return 이동된 게시물 응답 DTO
     */
    @PutMapping("/{postId}/category")
    public ResponseEntity<PostResponseDto> changePostCategory(
        @PathVariable Long postId,
        @Valid @RequestBody ChangePostCategoryRequestDto changePostCategoryRequestDto) {

        PostResponseDto postResponseDto = adminPostService.changePostCategory(postId,
            changePostCategoryRequestDto);
        return ResponseEntity.ok(postResponseDto);
    }
}