package com.sparta.deventer.dto;

import com.sparta.deventer.entity.Category;
import com.sparta.deventer.entity.Post;

import java.util.stream.Collectors;

public class PostResponseDto {
    private Long postid;
    private String title;
    private String content;
    private String categoryTopic;


    public PostResponseDto(Post post) {
        this.postid = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.categoryTopic = post.getCategory().getTopic();
    }

}
