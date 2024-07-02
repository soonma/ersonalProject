package com.sparta.deventer.custom;

import com.sparta.deventer.entity.Comment;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentRepositoryCustom {

    List<Comment> findAllByPostId(long id);

    Page<Comment> findByUserId(Long userId, Pageable pageable);
}
