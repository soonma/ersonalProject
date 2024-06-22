package com.sparta.deventer.controller;

import com.sparta.deventer.dto.CreatePostRequestDto;
import com.sparta.deventer.dto.PostResponseDto;
import com.sparta.deventer.dto.PostWithCommentsResponseDto;
import com.sparta.deventer.dto.UpdatePostRequestDto;
import com.sparta.deventer.security.UserDetailsImpl;
import com.sparta.deventer.service.PostService;
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

    private static final int PAGE_SIZE = 5;
    private final PostService postService;

    // 게시물 생성
    @PostMapping
    public ResponseEntity<PostResponseDto> createPost(
        @RequestBody CreatePostRequestDto createPostRequestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {

        PostResponseDto postResponseDto = postService.createPost(createPostRequestDto,
            userDetails.getUser());
        return ResponseEntity.status(HttpStatus.CREATED).body(postResponseDto);
    }

    // 전체 게시물 조회
    @GetMapping
    public ResponseEntity<Page<PostResponseDto>> getAllPosts(
        @RequestParam(defaultValue = "0") int page) {

        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        Page<PostResponseDto> posts = postService.getAllPosts(pageable);
        return ResponseEntity.ok(posts);
    }

    // 특정 게시물 및 해당 글의 댓글들 조회
    @GetMapping("/{postId}")
    public ResponseEntity<PostWithCommentsResponseDto> getPostWithComments(
        @PathVariable Long postId) {

        PostWithCommentsResponseDto postWithCommentsResponseDto =
            postService.getPostWithComments(postId);
        return ResponseEntity.ok().body(postWithCommentsResponseDto);
    }

    // 특정 카테고리 내의 모든 게시물 조회
    @GetMapping(params = "categoryId")
    public ResponseEntity<Page<PostResponseDto>> getPostsByCategory(@RequestParam Long categoryId,
        @RequestParam(defaultValue = "0") int page) {

        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        Page<PostResponseDto> postsResponseDtoPage = postService.getPostsByCategory(categoryId,
            pageable);
        return ResponseEntity.ok(postsResponseDtoPage);
    }

    // 게시물 수정
    @PutMapping("/{postId}")
    public ResponseEntity<PostResponseDto> updatePost(
        @PathVariable Long postId,
        @RequestBody UpdatePostRequestDto updatePostRequestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {

        PostResponseDto postResponseDto = postService.updatePost(postId, updatePostRequestDto,
            userDetails.getUser());
        return ResponseEntity.ok(postResponseDto);
    }

    // 게시물 삭제
    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {

        postService.deletePost(postId, userDetails.getUser());
        return ResponseEntity.noContent().build();
    }
}