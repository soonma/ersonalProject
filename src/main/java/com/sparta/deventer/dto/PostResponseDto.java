package com.sparta.deventer.dto;

import com.sparta.deventer.entity.Category;
import com.sparta.deventer.entity.Post;

public class PostResponseDto {
    private Long postid;
    private String title;
    private String content;
    private Category category;

    public PostResponseDto(Post post) {
        this.postid = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.category = post.getCategory();
    }
}
