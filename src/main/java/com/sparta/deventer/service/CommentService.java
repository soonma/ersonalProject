package com.sparta.deventer.service;

import com.sparta.deventer.dto.CommentRequestDto;
import com.sparta.deventer.dto.CommentResponseDto;
import com.sparta.deventer.entity.Comment;
import com.sparta.deventer.entity.Post;
import com.sparta.deventer.entity.User;
import com.sparta.deventer.enums.NotFoundEntity;
import com.sparta.deventer.enums.UserActionError;
import com.sparta.deventer.enums.UserStatus;
import com.sparta.deventer.exception.EntityNotFoundException;
import com.sparta.deventer.exception.MismatchStatusException;
import com.sparta.deventer.repository.CommentRepository;
import com.sparta.deventer.repository.PostRepository;
import com.sparta.deventer.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    /**
     * 댓글을 생성합니다.
     *
     * @param commentRequestDto 댓글 생성 요청 DTO
     * @param user              현재 인증된 사용자 정보
     * @return 생성된 댓글 응답 DTO
     */
    public CommentResponseDto createComment(CommentRequestDto commentRequestDto, User user) {
        validateUserStatus(user.getId());

        Post post = getPostByIdOrThrow(commentRequestDto.getPostId());
        Comment comment = new Comment(commentRequestDto.getContent(), user, post);

        commentRepository.save(comment);
        return new CommentResponseDto(comment);
    }

    /**
     * 댓글을 수정합니다.
     *
     * @param commentId         수정할 댓글 ID
     * @param commentRequestDto 댓글 수정 요청 DTO
     * @param user              현재 인증된 사용자 정보
     * @return 수정된 댓글 응답 DTO
     */
    @Transactional
    public CommentResponseDto updateComment(
        Long commentId,
        CommentRequestDto commentRequestDto,
        User user) {

        validateUserStatus(user.getId());

        Comment comment = getCommentByIdOrThrow(commentId);
        validateUserOwnership(comment, user.getId());
        comment.update(commentRequestDto.getContent());

        return new CommentResponseDto(comment);
    }

    /**
     * 댓글을 삭제합니다.
     *
     * @param commentId 삭제할 댓글 ID
     * @param user      현재 인증된 사용자 정보
     */
    public void deleteComment(Long commentId, User user) {
        validateUserStatus(user.getId());
        Comment comment = getCommentByIdOrThrow(commentId);
        validateUserOwnership(comment, user.getId());
        commentRepository.delete(comment);
    }

    /**
     * 사용자와 댓글 작성자가 일치하는지 확인합니다.
     *
     * @param comment 댓글 객체
     * @param userId  사용자 ID
     */
    private void validateUserOwnership(Comment comment, Long userId) {
        if (!comment.getUser().getId().equals(userId)) {
            throw new MismatchStatusException(UserActionError.NOT_AUTHORIZED);
        }
    }

    /**
     * 사용자 상태를 확인합니다. BLOCKED 상태인 경우 예외를 던집니다.
     *
     * @param userId 사용자 ID
     */
    private void validateUserStatus(Long userId) {
        User user = userRepository.findById((userId)).orElseThrow(
            () -> new EntityNotFoundException(NotFoundEntity.USER_NOT_FOUND));
        if (user.getStatus().equals(UserStatus.BLOCKED)) {
            throw new MismatchStatusException(UserActionError.BLOCKED_USER);
        }
    }

    /**
     * 댓글을 ID로 조회합니다. 존재하지 않으면 예외를 던집니다.
     *
     * @param commentId 조회할 댓글 ID
     * @return 조회된 댓글
     */
    private Comment getCommentByIdOrThrow(Long commentId) {
        return commentRepository.findById(commentId)
            .orElseThrow(() -> new EntityNotFoundException(NotFoundEntity.COMMENT_NOT_FOUND));
    }

    /**
     * 게시물을 ID로 조회합니다. 존재하지 않으면 예외를 던집니다.
     *
     * @param postId 조회할 게시물 ID
     * @return 조회된 게시물
     */
    private Post getPostByIdOrThrow(Long postId) {
        return postRepository.findById(postId)
            .orElseThrow(() -> new EntityNotFoundException(NotFoundEntity.POST_NOT_FOUND));
    }
}