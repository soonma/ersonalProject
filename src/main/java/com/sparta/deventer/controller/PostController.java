package com.sparta.deventer.controller;

import com.sparta.deventer.dto.PostRequestDto;
import com.sparta.deventer.dto.PostResponseDto;
import com.sparta.deventer.dto.UpdatePostRequestsDto;
import com.sparta.deventer.security.UserDetailsImpl;
import com.sparta.deventer.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    // 게시글 생성
    @PostMapping
    public ResponseEntity<String> createPost(
            @RequestBody PostRequestDto postRequestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        postService.createPost(postRequestDto, userDetails.getUser());
        return new ResponseEntity<>("게시글이 성공적으로 작성되었습니다.", HttpStatus.CREATED);
    }
    // 게시글 수정
    @PutMapping("/{postId}")
    public ResponseEntity<PostResponseDto> updatePost(
            @PathVariable Long postId,
            @RequestBody UpdatePostRequestsDto updatePostRequestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        PostResponseDto postResponseDto = postService.updatePost(postId, updatePostRequestDto, userDetails.getUser());
        return new ResponseEntity<>(postResponseDto, HttpStatus.OK);
    }
    //게시글 삭제
    @DeleteMapping("/{postId}")
    public ResponseEntity<String> deletePost(@PathVariable Long postId,
                                             @AuthenticationPrincipal UserDetailsImpl userDetails) {
        postService.deletePost(postId, userDetails.getUser());
        return new ResponseEntity<>("게시글이 삭제 되었습니다.", HttpStatus.OK);
    }
}