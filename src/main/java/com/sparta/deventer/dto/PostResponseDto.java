package com.sparta.deventer.dto;

import com.sparta.deventer.entity.Post;
import lombok.Getter;

@Getter
public class PostResponseDto {

    private final Long postId;
    private final String title;
    private final String content;
    private final String categoryTopic;

    public PostResponseDto(Post post) {
        this.postId = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.categoryTopic = post.getCategory().getTopic();
    }
}