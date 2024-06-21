package com.sparta.deventer.dto;

import java.util.List;
import lombok.Getter;

@Getter
public class PostWithCommentsResponseDto {

    private PostResponseDto responseDto;

    private List<CommentResponseDto> commentList;

    public PostWithCommentsResponseDto(PostResponseDto responseDto,
            List<CommentResponseDto> commentList) {
        this.responseDto = responseDto;
        this.commentList = commentList;
    }
}
