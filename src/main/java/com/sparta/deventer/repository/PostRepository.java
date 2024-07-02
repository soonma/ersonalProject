package com.sparta.deventer.repository;

import com.sparta.deventer.custom.PostRepositoryCustom;
import com.sparta.deventer.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PostRepository extends JpaRepository<Post, Long>, JpaSpecificationExecutor<Post>,
        PostRepositoryCustom {
//
//    @Override
//    Page<Post> findAllByCategory(Category category, Pageable pageable);
//
//    @Override
//    Page<Post> findAll(Pageable pageable);
//
//    @Override
//    Page<Post> findByUserId(Long userId, Pageable pageable);
}
