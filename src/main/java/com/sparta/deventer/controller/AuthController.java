package com.sparta.deventer.controller;

import com.sparta.deventer.dto.LoginRequestDto;
import com.sparta.deventer.dto.SignUpRequestDto;
import com.sparta.deventer.security.UserDetailsImpl;
import com.sparta.deventer.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
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
     * @param requestDto 일반유저 회원가입 Request
     * @return 회원가입 완료 메세지
     */
    @PostMapping("/auth/sign-up")
    public ResponseEntity<String> userSignUp(@Valid @RequestBody SignUpRequestDto requestDto) {
        return ResponseEntity.ok().body(authService.userSignUp(requestDto));
    }

    /**
     * 로그인 기능
     *
     * @param requestDto 로그인 ID와 패스워드
     * @param response   억세스토큰과 리플레시토큰 담아줄 Response
     * @return 회원가입 완료 메세지
     */
    @PostMapping("/auth/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequestDto requestDto,
            HttpServletResponse response) {
        return ResponseEntity.ok().body(authService.login(requestDto, response));
    }

    /**
     * 억세스토큰 재발행 기능
     *
     * @param request  기존의 토큰을 확인용으로 담아서 보낼 Request
     * @param response 새로운 토큰을 새로 담아서 줄 Response
     * @return 토큰 발행 완료 메세지
     */
    @PostMapping("/auth/refresh")
    public ResponseEntity<String> tokenReissue(HttpServletRequest request,
            HttpServletResponse response) {
        return ResponseEntity.ok().body(authService.tokenReissue(request, response));
    }

    /**
     * 로그아웃 기능
     *
     * @param userDetails 시큐리티 컨택스트에 담긴 유저정보(토큰검증후)
     * @return 로그아웃 만료 메세지
     */
    @PostMapping("/auth/logout")
    public ResponseEntity<String> logout(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok().body(authService.logout(userDetails.getUser().getId()));
    }

    /**
     * 회원탈퇴 기능
     *
     * @param userDetails 시큐리티 컨택스트에 담긴 유저정보(토큰검증후)
     * @return 회원탈퇴 만료 메세지
     */
    @DeleteMapping("/auth/withdraw")
    public ResponseEntity<String> withdraw(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok().body(authService.withdraw(userDetails.getUser().getId()));
    }
}