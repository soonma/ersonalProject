package com.sparta.deventer.service;

import com.sparta.deventer.dto.CommentResponseDto;
import com.sparta.deventer.dto.PostRequestDto;
import com.sparta.deventer.dto.PostResponseDto;
import com.sparta.deventer.dto.PostWithCommentsResponseDto;
import com.sparta.deventer.dto.UpdatePostRequestsDto;
import com.sparta.deventer.entity.Category;
import com.sparta.deventer.entity.Comment;
import com.sparta.deventer.entity.Post;
import com.sparta.deventer.entity.User;
import com.sparta.deventer.exception.CategoryNotFoundException;
import com.sparta.deventer.exception.PostNotFoundException;
import com.sparta.deventer.exception.UserNotFoundException;
import com.sparta.deventer.repository.CategoryRepository;
import com.sparta.deventer.repository.CommentRepository;
import com.sparta.deventer.repository.PostRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    private final CommentRepository commentRepository;

    public PostWithCommentsResponseDto getPostDetail(Long postId) {
        List<Comment> commentList = commentRepository.findAllByPostId(postId);
        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("게시글을 찾을 수 없습니다."));

        PostResponseDto postResponseDto = new PostResponseDto(post);

        for (Comment comment : commentList) {
            CommentResponseDto commentResponseDto = new CommentResponseDto(comment);
            commentResponseDtoList.add(commentResponseDto);
        }
        PostWithCommentsResponseDto responseDto = new PostWithCommentsResponseDto(postResponseDto,
                commentResponseDtoList);
        return responseDto;
    }

    // 게시글 생성
    public PostResponseDto createPost(PostRequestDto postRequestDto, User user) {
        Category category = categoryRepository.findByTopic(postRequestDto.getCategoryTopic())
                .orElseThrow(() -> new CategoryNotFoundException("카테고리를 찾을 수 없습니다."));

        Post post = new Post(postRequestDto.getTitle(), postRequestDto.getContent(), user, category);
        postRepository.save(post);
        return new PostResponseDto(post);
    }

    //게시글 전체 조회
    public List<PostResponseDto> getAllPosts(Pageable pageable) {
        Pageable sortedByCreatedAtDesc = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by("createdAt").descending()
        );
        Page<PostResponseDto> page = postRepository.findAll(sortedByCreatedAtDesc)
                .map(PostResponseDto::new);

        return page.getContent();
    }

    // 카테고리 내의 게시글 조회
    public List<PostResponseDto> getPostsByCategory(Long categoryId, Pageable pageable) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException("카테고리를 찾을 수 없습니다."));

        Pageable sortedByCreatedAtDesc = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by("createdAt").descending()
        );
        Page<PostResponseDto> page = postRepository.findAllByCategory(category, sortedByCreatedAtDesc)
                .map(PostResponseDto::new);

        return page.getContent();
    }

    // 게시글 수정
    public PostResponseDto updatePost(Long postId, UpdatePostRequestsDto updatePostRequestsDto, User user) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("게시글을 찾을 수 없습니다."));

        if (!post.getUser().getId().equals(user.getId())) {
            throw new UserNotFoundException("작성자만 수정할 수 있습니다.");
        }

        post.update(
                updatePostRequestsDto.getTitle(),
                updatePostRequestsDto.getContent());

        postRepository.save(post);
        return new PostResponseDto(post);
    }

    //게시글 삭제
    public void deletePost(Long postId, User user) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("게시글을 찾을 수 없습니다."));

        if (!post.getUser().getId().equals(user.getId())) {
            throw new UserNotFoundException("작성자만 삭제할 수 있습니다.");
        }

        postRepository.delete(post);
    }
}