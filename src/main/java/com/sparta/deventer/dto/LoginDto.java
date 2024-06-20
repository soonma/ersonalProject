package com.sparta.deventer.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto {

    @NotBlank(message = "아이디는 공백일 수 없습니다.")
    @Pattern(regexp = "^[a-z0-9!@#$%^&*()]{4,10}$",
            message = "아이디는 4자이상 10자 이하여야 하며 소문자, 숫자, 특수문자만 사용가능합니다.")
    private String username;

    @NotBlank(message = "패스워드는 공백일 수 없습니다.")
    @Pattern(regexp = "^[a-zA-Z0-9!@#$%^&*()]{8,15}$",
            message = "비밀번호는 8자이상 15자이하여야하고 영어 대소문자, 숫자, 특수문자만 사용가능합니다.")
    private String password;
}