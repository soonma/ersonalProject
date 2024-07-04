package com.sparta.deventer.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class FollowWithPostResponseDto {

    private FollowUserInfoResponseDto followUserInfoResponseDto;
    private List<PostResponseDto> responseDtos;

}
