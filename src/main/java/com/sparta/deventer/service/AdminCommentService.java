package com.sparta.deventer.service;

import com.sparta.deventer.dto.CommentRequestDto;
import com.sparta.deventer.dto.CommentResponseDto;
import com.sparta.deventer.entity.Comment;
import com.sparta.deventer.enums.NotFoundEntity;
import com.sparta.deventer.exception.EntityNotFoundException;
import com.sparta.deventer.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminCommentService {

    private final CommentRepository commentRepository;

    /**
     * 댓글을 업데이트합니다.
     *
     * @param commentId         댓글 ID
     * @param commentRequestDto 댓글 요청 DTO
     * @return 업데이트된 댓글의 응답 DTO
     */
    @Transactional
    public CommentResponseDto updateComment(Long commentId, CommentRequestDto commentRequestDto) {
        Comment comment = getCommentByIdOrThrow(commentId);
        validatePostIdMatch(commentRequestDto.getPostId(), comment.getPost().getId());
        comment.update(commentRequestDto.getContent());
        return new CommentResponseDto(comment);
    }

    /**
     * 댓글을 삭제합니다.
     *
     * @param commentId 댓글 ID
     */
    public void deleteComment(Long commentId) {
        Comment comment = getCommentByIdOrThrow(commentId);
        commentRepository.delete(comment);
    }

    /**
     * 게시글 ID가 일치하는지 확인합니다.
     *
     * @param requestPostId 요청된 게시글 ID
     * @param actualPostId  실제 게시글 ID
     */
    public void validatePostIdMatch(Long requestPostId, Long actualPostId) {
        if (!requestPostId.equals(actualPostId)) {
            throw new IllegalArgumentException("요청한 게시물 ID와 실제 게시물 ID가 일치하지 않습니다.");
        }
    }

    /**
     * 주어진 ID로 댓글을 찾거나, 존재하지 않으면 예외를 던집니다.
     *
     * @param commentId 댓글 ID
     * @return Comment 객체
     */
    public Comment getCommentByIdOrThrow(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(
            () -> new EntityNotFoundException(NotFoundEntity.COMMENT_NOT_FOUND));
    }
}