package com.sparta.deventer.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sparta.deventer.entity.Post;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class PostResponseDto {

    private String nickname;
    private String categoryTopic;
    private String title;
    private String content;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;

    @JsonIgnore
    private boolean someFieldToIgnore;


    public PostResponseDto(Post post) {
        this.nickname = post.getUser().getNickname();
        this.categoryTopic = post.getCategory().getTopic();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.createAt = post.getCreatedAt();
        this.updateAt = post.getUpdateAt();
    }

}
