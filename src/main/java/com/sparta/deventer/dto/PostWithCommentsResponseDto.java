package com.sparta.deventer.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostWithCommentsResponseDto {

    private final PostResponseDto postResponseDto;
    private final List<CommentWithLikeResponseDto> commentResponseDtos;
    private final long postLikeCount;

}