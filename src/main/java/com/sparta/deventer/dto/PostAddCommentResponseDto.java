package com.sparta.deventer.dto;

import com.sparta.deventer.entity.Post;
import java.util.List;
import lombok.Getter;

@Getter
public class PostAddCommentResponseDto {
    private PostResponseDto responseDto;
    private List<CommentResponseDto> commentList;
}
