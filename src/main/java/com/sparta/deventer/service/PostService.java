package com.sparta.deventer.service;

import com.sparta.deventer.dto.PostRequestDto;
import com.sparta.deventer.dto.PostResponseDto;
import com.sparta.deventer.dto.UpdatePostRequestsDto;
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
    public PostResponseDto createPost(PostRequestDto postRequestDto, User user) {
        Category category = categoryRepository.findById(postRequestDto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("카테고리를 찾을 수 없습니다."));

        Post post = new Post(postRequestDto.getTitle(), postRequestDto.getContent(), user, category);
        postRepository.save(post);
        return new PostResponseDto(post);
    }
    // 게시글 수정
    public PostResponseDto updatePost(Long postId, UpdatePostRequestsDto updatePostRequestsDto, User user) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시글을 찾을 수 없습니다."));

        if (!post.getUser().equals(user)) {
            throw new RuntimeException("작성자만 수정할 수 있습니다.");
        }

        post.update(
                updatePostRequestsDto.getTitle(),
                updatePostRequestsDto.getContent(),
                updatePostRequestsDto.getCategory());

        postRepository.save(post);
        return new PostResponseDto(post);
    }
}