package com.sparta.deventer.dto;

import com.sparta.deventer.entity.User;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class FollowWithPostResponseDto {

    private User followingUser;
    private List<PostResponseDto> responseDtos;

}
