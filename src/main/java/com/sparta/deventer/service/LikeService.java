package com.sparta.deventer.service;

import com.sparta.deventer.entity.Comment;
import com.sparta.deventer.entity.ContentEnumType;
import com.sparta.deventer.entity.Like;
import com.sparta.deventer.entity.Post;
import com.sparta.deventer.entity.User;
import com.sparta.deventer.enums.MismatchStatusEntity;
import com.sparta.deventer.enums.NotFoundEntity;
import com.sparta.deventer.exception.EntityNotFoundException;
import com.sparta.deventer.exception.MismatchStatusException;
import com.sparta.deventer.repository.CommentRepository;
import com.sparta.deventer.repository.LikeRepository;
import com.sparta.deventer.repository.PostRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;

    public Boolean likeComparison(String contentType, Long contentId, User user) {
        Optional<Like> like = likeRepository.findByContentIdAndContentTypeAndUserId(contentId,
                ContentEnumType.getByType(contentType), user.getId());

        if (like.isEmpty()) {
            CheckContent(contentType, contentId, user.getId());
            Like saveLike = new Like(user, contentId, contentType);
            likeRepository.save(saveLike);
            return true;
        } else {
            CheckContent(contentType, contentId, user.getId());
            likeRepository.delete(like.get());
            return false;
        }
    }

    public int likeCount(String contentType, Long contentId) {
        return likeRepository.findByContentIdAndContentType(contentId,
                ContentEnumType.getByType(contentType)).size();
    }

    public void CheckContent(String contentType, Long contentId, Long userId) {
        if (contentType.equals(ContentEnumType.POST.getType())) {
            Post post = postRepository.findById(contentId).orElseThrow(
                    () -> new EntityNotFoundException(NotFoundEntity.POST_NOT_FOUND));
            if (post.getUser().getId().equals(userId)) {
                throw new MismatchStatusException(MismatchStatusEntity.SELF_USER);
            }
        } else {
            Comment comment = commentRepository.findById(contentId).orElseThrow(
                    () -> new EntityNotFoundException(NotFoundEntity.COMMENT_NOT_FOUND));
            if (comment.getUser().getId().equals(userId)) {
                throw new MismatchStatusException(MismatchStatusEntity.SELF_USER);
            }
        }
    }
}

