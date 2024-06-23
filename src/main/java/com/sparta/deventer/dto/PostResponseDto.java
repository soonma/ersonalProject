package com.sparta.deventer.dto;


import com.sparta.deventer.entity.Post;
import com.sparta.deventer.entity.User;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostResponseDto {
    private Long postId;
    private Long userId;
    private String title;
    private String content;
    private Long categoryId;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;


    public PostResponseDto(Post post)
    {
        this.postId = post.getId();
        this.userId = post.getUser().getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.categoryId = post.getCategory().getId();
        this.createAt = post.getCreatedAt();
        this.updateAt = post.getUpdateAt();
    }

}
