package com.sparta.deventer.controller;

import com.sparta.deventer.dto.CreatePostRequestDto;
import com.sparta.deventer.dto.PostResponseDto;
import com.sparta.deventer.dto.PostWithCommentsResponseDto;
import com.sparta.deventer.dto.UpdatePostRequestDto;
import com.sparta.deventer.security.UserDetailsImpl;
import com.sparta.deventer.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private static final int PAGE_SIZE = 10;
    private final PostService postService;

    /**
     * 게시물을 생성합니다.
     *
     * @param createPostRequestDto 게시물 생성 요청 DTO
     * @param userDetails          현재 인증된 사용자 정보
     * @return 생성된 게시물 응답 DTO
     */
    @PostMapping
    public ResponseEntity<PostResponseDto> createPost(
        @Valid @RequestBody CreatePostRequestDto createPostRequestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {

        PostResponseDto postResponseDto = postService.createPost(createPostRequestDto,
            userDetails.getUser());
        return ResponseEntity.status(HttpStatus.CREATED).body(postResponseDto);
    }

    /**
     * 게시물 상세 정보를 조회합니다.
     *
     * @param postId 조회할 게시물 ID
     * @return 게시물 상세 정보 응답 DTO
     */
    @GetMapping("/{postId}")
    public ResponseEntity<PostWithCommentsResponseDto> getPostDetail(@PathVariable Long postId) {
        PostWithCommentsResponseDto postWithCommentsResponseDto = postService.getPostDetail(postId);
        return ResponseEntity.ok().body(postWithCommentsResponseDto);
    }

    /**
     * 전체 게시물을 조회합니다.
     *
     * @param page 페이지 번호
     * @return 페이지네이션된 게시물 목록
     */
    @GetMapping
    public ResponseEntity<Page<PostResponseDto>> getAllPosts(
        @RequestParam(defaultValue = "0") int page) {

        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        Page<PostResponseDto> postResponseDtoPage = postService.getAllPosts(pageable);
        return ResponseEntity.ok(postResponseDtoPage);
    }

    /**
     * 카테고리별 게시물을 조회합니다.
     *
     * @param categoryId 카테고리 ID
     * @param page       페이지 번호
     * @return 페이지네이션된 카테고리별 게시물 목록
     */
    @GetMapping(params = "category")
    public ResponseEntity<Page<PostResponseDto>> getPostsByCategory(
        @RequestParam Long categoryId,
        @RequestParam(defaultValue = "0") int page) {

        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        Page<PostResponseDto> postResponseDtoPage = postService.getPostsByCategory(categoryId,
            pageable);
        return ResponseEntity.ok(postResponseDtoPage);
    }

    /**
     * 게시물을 수정합니다.
     *
     * @param postId               수정할 게시물 ID
     * @param updatePostRequestDto 게시물 수정 요청 DTO
     * @param userDetails          현재 인증된 사용자 정보
     * @return 수정된 게시물 응답 DTO
     */
    @PutMapping("/{postId}")
    public ResponseEntity<PostResponseDto> updatePost(
        @PathVariable Long postId,
        @Valid @RequestBody UpdatePostRequestDto updatePostRequestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {

        PostResponseDto postResponseDto = postService.updatePost(postId, updatePostRequestDto,
            userDetails.getUser());
        return ResponseEntity.ok(postResponseDto);
    }

    /**
     * 게시물을 삭제합니다.
     *
     * @param postId      삭제할 게시물 ID
     * @param userDetails 현재 인증된 사용자 정보
     * @return 삭제 완료 메시지
     */
    @DeleteMapping("/{postId}")
    public ResponseEntity<String> deletePost(
        @PathVariable Long postId,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {

        postService.deletePost(postId, userDetails.getUser());
        return ResponseEntity.ok().body("게시물을 삭제했습니다.");
    }
}