package com.sparta.deventer.service;

import com.sparta.deventer.entity.Comment;
import com.sparta.deventer.entity.ContentEnumType;
import com.sparta.deventer.entity.Like;
import com.sparta.deventer.entity.Post;
import com.sparta.deventer.exception.CommentNotFoundException;
import com.sparta.deventer.exception.PostNotFoundException;
import com.sparta.deventer.repository.CommentRepository;
import com.sparta.deventer.repository.LikeRepository;
import com.sparta.deventer.repository.PostRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeService {

    private static final Logger log = LoggerFactory.getLogger(LikeService.class);
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;

    public Boolean likeComparison(String contentType, Long contentId, Long userId) {
        Optional<Like> like = likeRepository.findByContentIdAndContentTypeAndUserId(contentId,
                ContentEnumType.getByType(contentType), userId);
        log.info("{}", like.isEmpty());

        if (like.isEmpty()) {
            CheckContent(contentType, contentId, userId);
            Like saveLike = new Like(userId, contentId, contentType);
            likeRepository.save(saveLike);
            return true;
        } else {
            CheckContent(contentType, contentId, userId);
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
                    () -> new PostNotFoundException("게시글이 존재 하지 않습니다."));
            if (post.getUser().getId().equals(userId)) {
                throw new IllegalArgumentException("본인이 좋아요 할수 없습니다");
            }
        } else {
            Comment comment = commentRepository.findById(contentId).orElseThrow(
                    () -> new CommentNotFoundException("댓글이 존재 하지 않습니다."));
            if (comment.getUser().getId().equals(userId)) {
                throw new IllegalArgumentException("본인이 좋아요 할수 없습니다");
            }
        }
    }
}

