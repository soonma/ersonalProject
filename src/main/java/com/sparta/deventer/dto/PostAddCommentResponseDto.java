package com.sparta.deventer.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;

@Getter
public class PostAddCommentResponseDto {

    private PostResponseDto responseDto;

    private List<CommentResponseDto> commentList;

    public PostAddCommentResponseDto(PostResponseDto responseDto, List<CommentResponseDto> commentList) {
        this.responseDto = responseDto;
        this.commentList = commentList;
    }
}
