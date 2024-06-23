package com.sparta.deventer.repository;

import com.sparta.deventer.entity.Category;
import com.sparta.deventer.entity.Post;
import com.sparta.deventer.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Range;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long>{
    Page<Post> findAllByCategory(Category category, Pageable pageable);
    Page<Post> findAll(Pageable pageable);
    Optional<Post> findById(Long postid);

}
