package com.sparta.deventer.service;

import com.sparta.deventer.dto.CommentRequestDto;
import com.sparta.deventer.dto.CommentResponseDto;
import com.sparta.deventer.entity.Comment;
import com.sparta.deventer.enums.UserRole;
import com.sparta.deventer.repository.CommentRepository;
import com.sparta.deventer.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminCommentService {

    private final CommentRepository commentRepository;

    public CommentResponseDto updateComment(UserDetailsImpl userDetails,
            CommentRequestDto requestDto, Long commentId) {
        checkAdminUser(userDetails.getUser().getRole());

        Comment comment = emptyCheckComment(commentId);

        comment.update(requestDto.getContent());

        return new CommentResponseDto(comment);
    }

    public void deleteComment(UserDetailsImpl userDetails, Long commentId) {
        checkAdminUser(userDetails.getUser().getRole());

        Comment comment = emptyCheckComment(commentId);

        commentRepository.delete(comment);
    }


    public void checkAdminUser(UserRole role) {
        if (!role.equals(UserRole.ADMIN)) {
            throw new IllegalArgumentException("관리자가 아닙니다.");
        }
    }

    public Comment emptyCheckComment(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(
                () -> new IllegalArgumentException("댓글이 존재 하지 않습니다."));
    }

}
