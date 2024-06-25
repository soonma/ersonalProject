package com.sparta.deventer.controller;

import com.sparta.deventer.dto.CommentRequestDto;
import com.sparta.deventer.dto.CommentResponseDto;
import com.sparta.deventer.service.AdminCommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/comments/{commentId}")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminCommentController {

    private final AdminCommentService adminCommentService;

    /**
     * 관리자 권한으로 댓글을 수정합니다.
     *
     * @param commentId         수정할 댓글 ID
     * @param commentRequestDto 댓글 수정 정보
     * @return 수정된 댓글 정보
     */
    @PutMapping
    public ResponseEntity<CommentResponseDto> adminUpdateComment(
        @PathVariable Long commentId,
        @Valid @RequestBody CommentRequestDto commentRequestDto) {

        CommentResponseDto commentResponseDto = adminCommentService.updateComment(commentId,
            commentRequestDto);
        return ResponseEntity.ok(commentResponseDto);
    }

    /**
     * 관리자 권한으로 댓글을 삭제합니다.
     *
     * @param commentId 삭제할 댓글 ID
     * @return 삭제 완료 메시지
     */
    @DeleteMapping
    public ResponseEntity<String> deleteComment(@PathVariable Long commentId) {

        adminCommentService.deleteComment(commentId);
        return ResponseEntity.ok("관리자 권한으로 댓글을 삭제했습니다.");
    }
}