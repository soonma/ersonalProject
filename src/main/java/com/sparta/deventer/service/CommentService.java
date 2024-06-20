package com.sparta.deventer.service;

import com.sparta.deventer.dto.CommentResponseDto;
import com.sparta.deventer.entity.Comment;
import com.sparta.deventer.repository.CommentRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository ;

    public List<CommentResponseDto> getCommnetList(Long postId) {
        List<Comment> commentList = commentRepository.findAllByPostId(postId);
        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();

        for (Comment comment : commentList) {
            CommentResponseDto commentResponseDto = new CommentResponseDto(comment);
            commentResponseDtoList.add(commentResponseDto);
        }
        return commentResponseDtoList;
    }
}
