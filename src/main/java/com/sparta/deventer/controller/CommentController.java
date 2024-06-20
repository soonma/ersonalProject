package com.sparta.deventer.controller;

import com.sparta.deventer.dto.CommentRequestDto;
import com.sparta.deventer.dto.CommentResponseDto;
import com.sparta.deventer.security.UserDetailsImpl;
import com.sparta.deventer.service.CommentService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/{postId}")
    public ResponseEntity<List<CommentResponseDto>> getCommentList(@PathVariable Long postId) {
        List<CommentResponseDto> commentList = commentService.getCommnetList(postId);
        return ResponseEntity.ok().body(commentList);
    }

    @PostMapping
    public ResponseEntity<CommentResponseDto> createComment(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody CommentRequestDto requestDto) {

        log.info("userDetails 정보 : {}", userDetails.getUser());
        log.info("requestDto 정보 : {}", requestDto);

        CommentResponseDto commentResponseDto = commentService.createComment(requestDto,
                userDetails.getUser());

        return ResponseEntity.ok().body(commentResponseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommentResponseDto> upateComment(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody CommentRequestDto requestDto,
            @PathVariable Long id
    ){
        return ResponseEntity.ok().body(commentService.updateComment(userDetails.getUser(),requestDto,id));
    }



}
