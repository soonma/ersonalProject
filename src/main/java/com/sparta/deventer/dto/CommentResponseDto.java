package com.sparta.deventer.dto;


import com.sparta.deventer.entity.Comment;
import com.sparta.deventer.entity.Post;
import com.sparta.deventer.entity.User;
import lombok.Getter;

@Getter
public class CommentResponseDto {
    private String content;
    private Long id;
    private User user;
    private Post post;

    public CommentResponseDto(Comment comment) {
        this.content =comment.getContent();
        this.id = comment.getId();
        this.user = comment.getUser();
        this.post = comment.getPost();
    }
}
