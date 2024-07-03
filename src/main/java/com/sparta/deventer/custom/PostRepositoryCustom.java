package com.sparta.deventer.custom;

import com.sparta.deventer.entity.Category;
import com.sparta.deventer.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostRepositoryCustom {

    Page<Post> findAllByCategory(Category category, Pageable pageable);

    Page<Post> findAll(Pageable pageable);

    Page<Post> findByUserId(Long userId, Pageable pageable);

}
