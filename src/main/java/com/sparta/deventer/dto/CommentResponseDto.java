package com.sparta.deventer.dto;


import com.sparta.deventer.entity.Comment;
import com.sparta.deventer.entity.Post;
import com.sparta.deventer.entity.User;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class CommentResponseDto {
    private String content;
    private Long id;
    private User user;
//    private Post post;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;


    public CommentResponseDto(Comment comment) {
        this.content =comment.getContent();
        this.id = comment.getId();
        this.user = comment.getUser();
//        this.post = comment.getPost();
        this.createAt = comment.getCreatedAt();
        this.updateAt = comment.getUpdateAt();
    }
}
