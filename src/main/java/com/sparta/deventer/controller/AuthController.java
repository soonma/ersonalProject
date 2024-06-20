package com.sparta.deventer.controller;

import com.sparta.deventer.dto.LoginDto;
import com.sparta.deventer.dto.SignUpUserDto;
import com.sparta.deventer.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * 일반유저 회원가입
     *
     * @param requestDto 일반 유저 회원가입시 필요 정보
     * @return 회원가입 성공 메세지
     */
    @PostMapping("/auth/sign-up")
    public ResponseEntity<String> signUp(@Valid @RequestBody SignUpUserDto requestDto) {
        return ResponseEntity.ok().body(authService.signUp(requestDto));
    }

    @PostMapping("/auth/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginDto requestDto,
            HttpServletResponse response) {
        return ResponseEntity.ok().body(authService.login(requestDto, response));
    }

    @PostMapping("/auth/refresh")
    public ResponseEntity<String> tokenReissue(HttpServletRequest request,
            HttpServletResponse response) {
        return ResponseEntity.ok().body(authService.tokenReissue(request, response));
    }
}
