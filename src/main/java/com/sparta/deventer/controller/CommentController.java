package com.sparta.deventer.controller;

import com.sparta.deventer.dto.CommentRequestDto;
import com.sparta.deventer.dto.CommentResponseDto;
import com.sparta.deventer.security.UserDetailsImpl;
import com.sparta.deventer.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    /**
     * 댓글을 생성합니다.
     *
     * @param commentRequestDto 댓글 생성 요청 DTO
     * @param userDetails       현재 인증된 사용자 정보
     * @return 생성된 댓글 응답 DTO
     */
    @PostMapping
    public ResponseEntity<CommentResponseDto> createComment(
        @Valid @RequestBody CommentRequestDto commentRequestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {

        CommentResponseDto commentResponseDto = commentService.createComment(commentRequestDto,
            userDetails.getUser());
        return ResponseEntity.status(HttpStatus.CREATED).body(commentResponseDto);
    }

    /**
     * 댓글을 수정합니다.
     *
     * @param commentId         수정할 댓글 ID
     * @param commentRequestDto 댓글 수정 요청 DTO
     * @param userDetails       현재 인증된 사용자 정보
     * @return 수정된 댓글 응답 DTO
     */
    @PutMapping("/{commentId}")
    public ResponseEntity<CommentResponseDto> updateComment(
        @PathVariable Long commentId,
        @Valid @RequestBody CommentRequestDto commentRequestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {

        return ResponseEntity.ok()
            .body(commentService.updateComment(commentId, commentRequestDto,
                userDetails.getUser()));
    }

    /**
     * 댓글을 삭제합니다.
     *
     * @param commentId   삭제할 댓글 ID
     * @param userDetails 현재 인증된 사용자 정보
     * @return 삭제 완료 메시지
     */
    @DeleteMapping("/{commentId}")
    public String deleteComment(
        @PathVariable Long commentId,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {

        commentService.deleteComment(commentId, userDetails.getUser());
        return "댓글을 삭제했습니다.";
    }
}