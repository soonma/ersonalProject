package com.sparta.deventer.repository;

import com.sparta.deventer.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

}
