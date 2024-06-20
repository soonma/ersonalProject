package com.sparta.deventer.service;

import com.sparta.deventer.dto.CreatePostRequestDto;
import com.sparta.deventer.dto.PostResponseDto;
import com.sparta.deventer.entity.Category;
import com.sparta.deventer.entity.Post;
import com.sparta.deventer.entity.User;
import com.sparta.deventer.repository.CategoryRepository;
import com.sparta.deventer.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;

    // 게시글 생성
    public PostResponseDto createPost(CreatePostRequestDto createPostRequestDto, User user) {
        Category category = categoryRepository.findById(createPostRequestDto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("카테고리를 찾을 수 없습니다."));

        Post post = new Post(createPostRequestDto.getTitle(), createPostRequestDto.getContent(), user, category);
        postRepository.save(post);
        return new PostResponseDto(post);
    }
}