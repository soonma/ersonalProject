package com.sparta.deventer.dto;

import com.sparta.deventer.entity.User;
import lombok.Getter;

@Getter
public class FollowUserInfoResponseDto {

    private final Long userId;
    private final String username;
    private final String nickname;
    private final String email;

    public FollowUserInfoResponseDto(User user) {
        this.userId = user.getId();
        this.username = user.getUsername();
        this.nickname = user.getNickname();
        this.email = user.getEmail();
    }
}
