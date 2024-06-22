package com.sparta.deventer.service;

import com.sparta.deventer.dto.CommentResponseDto;
import com.sparta.deventer.dto.CreatePostRequestDto;
import com.sparta.deventer.dto.PostResponseDto;
import com.sparta.deventer.dto.PostWithCommentsResponseDto;
import com.sparta.deventer.dto.UpdatePostRequestDto;
import com.sparta.deventer.entity.Category;
import com.sparta.deventer.entity.Post;
import com.sparta.deventer.entity.User;
import com.sparta.deventer.exception.CategoryNotFoundException;
import com.sparta.deventer.exception.PostNotFoundException;
import com.sparta.deventer.repository.CategoryRepository;
import com.sparta.deventer.repository.CommentRepository;
import com.sparta.deventer.repository.PostRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    private final CommentRepository commentRepository;

    // 게시물 생성
    public PostResponseDto createPost(CreatePostRequestDto createPostRequestDto, User user) {
        String title = createPostRequestDto.getTitle();
        String content = createPostRequestDto.getContent();
        Long categoryId = createPostRequestDto.getCategoryId();
        Category category = categoryRepository.findById(categoryId)
            .orElseThrow(() -> new CategoryNotFoundException("카테고리를 찾을 수 없습니다."));

        Post post = new Post(title, content, user, category);
        postRepository.save(post);
        return new PostResponseDto(post);
    }

    // 전체 게시물 조회
    public Page<PostResponseDto> getAllPosts(Pageable pageable) {
        return postRepository.findAll(pageable).map(PostResponseDto::new);
    }

    // 특정 게시물 및 해당 글의 댓글들 조회
    public PostWithCommentsResponseDto getPostWithComments(Long postId) {
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new PostNotFoundException("게시글이 존재 하지 않습니다."));

        List<CommentResponseDto> commentResponseDtoList = commentRepository.findAllByPostId(postId)
            .stream()
            .map(CommentResponseDto::new)
            .collect(Collectors.toList());

        return new PostWithCommentsResponseDto(new PostResponseDto(post), commentResponseDtoList);
    }

    // 특정 카테고리 내의 모든 게시물 조회
    public Page<PostResponseDto> getPostsByCategory(Long categoryId, Pageable pageable) {
        Category category = categoryRepository.findById(categoryId)
            .orElseThrow(() -> new CategoryNotFoundException("카테고리를 찾을 수 없습니다."));

        return postRepository.findAllByCategory(category, pageable)
            .map(PostResponseDto::new);
    }

    // 게시물 수정
    public PostResponseDto updatePost(Long postId, UpdatePostRequestDto updatePostRequestDto,
        User user) {

        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new PostNotFoundException("게시글을 찾을 수 없습니다."));

        user.validateId(post.getUser().getId());

        post.setTitle(updatePostRequestDto.getTitle());
        post.setContent(updatePostRequestDto.getContent());

        postRepository.save(post);
        return new PostResponseDto(post);
    }

    //게시물 삭제
    public void deletePost(Long postId, User user) {
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new PostNotFoundException("게시글을 찾을 수 없습니다."));

        user.validateId(post.getUser().getId());
        postRepository.delete(post);
    }
}