package com.sparta.deventer.dto;

import com.sparta.deventer.entity.User;
import lombok.Getter;

@Getter
public class ProfileResponseDto {

    private final Long userId;
    private final String username;
    private final String nickname;
    private final String email;
    private final long postLikeCount;
    private final long commentLikeCount;

    public ProfileResponseDto(User user, long postLikeCount, long commentLikeCount) {
        this.userId = user.getId();
        this.username = user.getUsername();
        this.nickname = user.getNickname();
        this.email = user.getEmail();
        this.postLikeCount = postLikeCount;
        this.commentLikeCount = commentLikeCount;
    }
}