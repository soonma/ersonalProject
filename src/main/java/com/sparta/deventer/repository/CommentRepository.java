package com.sparta.deventer.repository;

import com.sparta.deventer.custom.CommentRepositoryCustom;
import com.sparta.deventer.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryCustom {

//    List<Comment> findAllByPostId(long id);
//
//    Page<Comment> findByUserId(Long userId, Pageable pageable);
}