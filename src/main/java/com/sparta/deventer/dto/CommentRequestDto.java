package com.sparta.deventer.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CommentRequestDto {

    @NotBlank(message = "게시물 ID를 입력해야 합니다.")
    private Long postId;

    @NotBlank(message = "댓글 내용을 입력해야 합니다.")
    private String content;
}
