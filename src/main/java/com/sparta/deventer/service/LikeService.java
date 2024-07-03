package com.sparta.deventer.service;

import com.sparta.deventer.dto.CommentResponseDto;
import com.sparta.deventer.dto.PostResponseDto;
import com.sparta.deventer.entity.Comment;
import com.sparta.deventer.entity.Like;
import com.sparta.deventer.entity.LikeableEntityType;
import com.sparta.deventer.entity.Post;
import com.sparta.deventer.entity.User;
import com.sparta.deventer.enums.NotFoundEntity;
import com.sparta.deventer.enums.UserActionError;
import com.sparta.deventer.exception.EntityNotFoundException;
import com.sparta.deventer.exception.MismatchStatusException;
import com.sparta.deventer.repository.CommentRepository;
import com.sparta.deventer.repository.LikeRepository;
import com.sparta.deventer.repository.PostRepository;
import com.sparta.deventer.security.UserDetailsImpl;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final LikeRepository likeRepository;

    /**
     * 좋아요를 처리합니다. 이미 좋아요가 되어있으면 취소하고, 그렇지 않으면 좋아요를 추가합니다.
     *
     * @param likeableEntityId   좋아요를 할 엔티티의 ID
     * @param likeableEntityType 좋아요를 할 엔티티의 타입 (POST 또는 COMMENT)
     * @param user               현재 인증된 사용자 정보
     * @return 좋아요가 추가되었으면 true, 취소되었으면 false
     */
    @Transactional
    public boolean toggleLike(Long likeableEntityId, String likeableEntityType, User user) {
        LikeableEntityType entityType = LikeableEntityType.getByType(likeableEntityType);
        Optional<Like> optionalLike = likeRepository.findByLikeableEntityIdAndLikeableEntityTypeAndUserId(
                likeableEntityId, entityType, user.getId());

        if (optionalLike.isEmpty()) {
            validateLikeableEntityOwnership(likeableEntityId, entityType, user);
            Like saveLike = new Like(likeableEntityId, likeableEntityType, user);
            likeRepository.save(saveLike);
            return true;
        } else {
            validateLikeableEntityOwnership(likeableEntityId, entityType, user);
            likeRepository.delete(optionalLike.get());
            return false;
        }
    }

    /**
     * 특정 엔티티에 대한 좋아요 개수를 반환합니다.
     *
     * @param likeableEntityId   좋아요를 할 엔티티의 ID
     * @param likeableEntityType 좋아요를 할 엔티티의 타입 (POST 또는 COMMENT)
     * @return 좋아요 개수
     */
    public int likeCount(Long likeableEntityId, String likeableEntityType) {
        return likeRepository.findAllByLikeableEntityIdAndLikeableEntityType(likeableEntityId,
                LikeableEntityType.getByType(likeableEntityType)).size();
    }

    /**
     * 특정 엔티티의 소유권을 검증합니다. 사용자가 자신의 엔티티에 좋아요를 할 수 없도록 합니다.
     *
     * @param likeableEntityId   좋아요를 할 엔티티의 ID
     * @param likeableEntityType 좋아요를 할 엔티티의 타입 (POST 또는 COMMENT)
     * @param user               현재 인증된 사용자
     */
    private void validateLikeableEntityOwnership(
            Long likeableEntityId,
            LikeableEntityType likeableEntityType,
            User user) {

        if (likeableEntityType == LikeableEntityType.POST) {
            Post post = postRepository.findById(likeableEntityId).orElseThrow(
                    () -> new EntityNotFoundException(NotFoundEntity.POST_NOT_FOUND));
            if (post.getUser().getId().equals(user.getId())) {
                throw new MismatchStatusException(UserActionError.SELF_ACTION_NOT_ALLOWED);
            }
        } else if (likeableEntityType == LikeableEntityType.COMMENT) {
            Comment comment = commentRepository.findById(likeableEntityId).orElseThrow(
                    () -> new EntityNotFoundException(NotFoundEntity.COMMENT_NOT_FOUND));
            if (comment.getUser().getId().equals(user.getId())) {
                throw new MismatchStatusException(UserActionError.SELF_ACTION_NOT_ALLOWED);
            }
        } else {
            throw new IllegalArgumentException("좋아요를 할 수 없는 엔티티 타입입니다.");
        }
    }

    /**
     * 내가 좋아요한 게시글의 정보를 조회 합니다
     *
     * @param userDetails        인가된 유저 정보
     * @param likeableEntityType 좋아요를 할 엔티티의 타입 (POST 또는 COMMENT)
     * @param pageable           페이지 요청 정보
     */

    public Page<PostResponseDto> myLikePost(String likeableEntityType,
            UserDetailsImpl userDetails, Pageable pageable) {

        Pageable sortedByCreatedAtDesc = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by("createdAt").descending()
        );

        List<Like> likes = likeRepository
                .findAllByLikeableEntityTypeAndUserId(
                        LikeableEntityType.getByType(likeableEntityType),
                        userDetails.getUser().getId());

        List<Post> posts = new ArrayList<>();
        for (Like like : likes) {
            posts.add(postRepository.findById(like.getLikeableEntityId()).get());
        }

        List<PostResponseDto> responseDtoList = new ArrayList<>();
        for (Post post : posts) {
            responseDtoList.add(new PostResponseDto(post));
        }
        return new PageImpl<>(responseDtoList, sortedByCreatedAtDesc, posts.size());
    }

    /**
     * 내가 좋아요한 댓글의 정보를 조회 합니다
     *
     * @param userDetails        인가된 유저 정보
     * @param likeableEntityType 좋아요를 할 엔티티의 타입 (POST 또는 COMMENT)
     * @param pageable           페이지 요청 정보
     */
    public Page<CommentResponseDto> myLikeComment(String likeableEntityType,
            UserDetailsImpl userDetails, Pageable pageable) {

        Pageable sortedByCreatedAtDesc = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by("createdAt").descending()
        );

        List<Like> likes = likeRepository
                .findAllByLikeableEntityTypeAndUserId(
                        LikeableEntityType.getByType(likeableEntityType),
                        userDetails.getUser().getId());

        List<Comment> comments = new ArrayList<>();
        for (Like like : likes) {
            comments.add(commentRepository.findById(like.getLikeableEntityId()).get());
        }

        List<CommentResponseDto> responseDtoList = new ArrayList<>();
        for (Comment comment : comments) {
            responseDtoList.add(new CommentResponseDto(comment));
        }
        return new PageImpl<>(responseDtoList, sortedByCreatedAtDesc, comments.size());
    }
}

