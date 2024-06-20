package com.sparta.deventer.controller;

import com.sparta.deventer.dto.CommentResponseDto;
import com.sparta.deventer.service.CommentService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class CommentController {
    CommentService commentService;

    @GetMapping("{postId}")
    public ResponseEntity<List<CommentResponseDto>> getCommentList(@PathVariable Long postId) {
        List<CommentResponseDto> commentList = commentService.getCommnetList(postId);
        return ResponseEntity.ok().body(commentList);
    }
}
