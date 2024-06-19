package com.sparta.notfound.repository;

import com.sparta.notfound.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

}
