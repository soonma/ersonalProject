package com.sparta.deventer.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class UpdateProfileRequestDto {

    @NotBlank(message = "닉네임을 입력해야 합니다.")
    @Pattern(regexp = "^[a-z0-9_]+$", message = "닉네임은 소문자, 숫자, 밑줄(_)만 사용할 수 있습니다.")
    String nickname;

    @NotBlank(message = "이메일을 입력해야 합니다.")
    @Email(message = "올바른 이메일 형식이어야 합니다.")
    String email;
}