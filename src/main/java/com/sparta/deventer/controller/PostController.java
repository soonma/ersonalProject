package com.sparta.deventer.controller;

import com.sparta.deventer.dto.CreatePostRequestDto;
import com.sparta.deventer.security.UserDetailsImpl;
import com.sparta.deventer.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    // 게시글 생성
    @PostMapping
    public ResponseEntity<String> createPost(
            @RequestBody CreatePostRequestDto createPostRequestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        postService.createPost(createPostRequestDto, userDetails.getUser());
        return new ResponseEntity<>("게시글이 성공적으로 작성되었습니다.", HttpStatus.CREATED);
    }
}