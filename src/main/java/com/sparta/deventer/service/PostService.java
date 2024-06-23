package com.sparta.deventer.service;

import com.sparta.deventer.dto.*;
import com.sparta.deventer.entity.Category;
import com.sparta.deventer.entity.Post;
import com.sparta.deventer.entity.User;
import com.sparta.deventer.exception.DataNotFoundException;
import com.sparta.deventer.exception.UnauthorizedException;
import com.sparta.deventer.repository.CategoryRepository;
import com.sparta.deventer.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;


    // 게시글 생성
    public PostResponseDto createPost(PostRequestDto postRequestDto, User user) {
        Category category = categoryRepository.findByTopic(postRequestDto.getCategoryTopic())
                .orElseThrow(() -> new DataNotFoundException("카테고리를 찾을 수 없습니다."));


        Post post = new Post(postRequestDto.getTitle(), postRequestDto.getContent(), user, category);
        postRepository.save(post);
        return new PostResponseDto(post);
    }

    // 전체 게시글 조회
    public Page<PostResponseDto> getAllPosts(Pageable pageable) {
        return postRepository.findAll(pageable)
                .map(PostResponseDto::new);
    }

    // 카테고리 내의 게시글 조회
    public Page<PostResponseDto> getPostsByCategory(Long categoryId, Pageable pageable) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new DataNotFoundException("카테고리를 찾을 수 없습니다."));

        return postRepository.findAllByCategory(category, pageable)
                .map(PostResponseDto::new);
    }

    // 게시글 수정
    public PostResponseDto updatePost(Long postId, UpdatePostRequestsDto updatePostRequestsDto, User user) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new DataNotFoundException("게시글을 찾을 수 없습니다."));

        if (!post.getUser().equals(user)) {
            throw new UnauthorizedException("작성자만 수정할 수 있습니다.");
        }

        post.update(
                updatePostRequestsDto.getTitle(),
                updatePostRequestsDto.getContent(),
                updatePostRequestsDto.getCategory());

        postRepository.save(post);
        return new PostResponseDto(post);
    }

    //게시글 삭제
    public void deletePost(Long postId, User user) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new DataNotFoundException("게시글을 찾을 수 없습니다."));

        if (!post.getUser().equals(user)) {
            throw new UnauthorizedException("작성자만 삭제할 수 있습니다.");
        }

        postRepository.delete(post);
    }
}
