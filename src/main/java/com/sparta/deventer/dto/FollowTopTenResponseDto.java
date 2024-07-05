package com.sparta.deventer.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class FollowTopTenResponseDto {

    private final FollowUserInfoResponseDto followUserInfoResponseDto;
    private final long followCount;

}
