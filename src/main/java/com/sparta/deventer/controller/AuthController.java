package com.sparta.deventer.controller;

import com.sparta.deventer.dto.LoginRequestDto;
import com.sparta.deventer.dto.SignUpRequestDto;
import com.sparta.deventer.security.UserDetailsImpl;
import com.sparta.deventer.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * 사용자가 회원가입을 합니다.
     *
     * @param signUpRequestDto 회원가입 요청 DTO
     * @return 회원가입 완료 메세지
     */
    @PostMapping("/auth/sign-up")
    public ResponseEntity<String> userSignUp(
        @Valid @RequestBody SignUpRequestDto signUpRequestDto) {

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(authService.userSignUp(signUpRequestDto));
    }

    /**
     * 사용자가 로그인합니다.
     *
     * @param loginRequestDto 로그인 요청 DTO
     * @param response        억세스 토큰과 리프레시 토큰을 담아 줄 응답
     * @return 회원가입 완료 메세지
     */
    @PostMapping("/auth/login")
    public ResponseEntity<String> login(
        @Valid @RequestBody LoginRequestDto loginRequestDto,
        HttpServletResponse response) {

        return ResponseEntity.ok().body(authService.login(loginRequestDto, response));
    }

    /**
     * 사용자가 Github 계정으로 로그인합니다.
     *
     * @param code     사용자가 Github으로부터 받은 인증코드
     * @param response 사용자에게 Deventer 서비스의 액세스 토큰과 리프레시 토큰을 반환할 Response
     * @return 성공 메세지
     */
    @GetMapping("/github/callback")
    public String githubCallback(@RequestParam("code") String code, HttpServletResponse response) {
        authService.processGitHubCallback(code, response);
        return "회원가입 및 로그인에 성공했습니다.";
    }

    /**
     * 토큰을 재발행합니다.
     *
     * @param request  기존의 토큰을 확인하기 위해 담을 Request
     * @param response 새로운 토큰을 전달하기 위해 담을 Response
     * @return 토큰 발행 완료 메세지
     */
    @PostMapping("/auth/refresh")
    public ResponseEntity<String> tokenReissue(
        HttpServletRequest request,
        HttpServletResponse response) {

        return ResponseEntity.ok().body(authService.tokenReissue(request, response));
    }

    /**
     * 사용자가 로그아웃합니다.
     *
     * @param userDetails 시큐리티 컨택스트에 담긴 사용자 정보 (토큰 검증 후)
     * @return 로그아웃 완료 메시지
     */
    @PostMapping("/auth/logout")
    public ResponseEntity<String> logout(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok().body(authService.logout(userDetails.getUser().getId()));
    }

    /**
     * 사용자가 회원을 탈퇴합니다.
     *
     * @param userDetails 시큐리티 컨택스트에 담긴 사용자 정보 (토큰 검증 후)
     * @return 회원탈퇴 완료 메시지
     */
    @DeleteMapping("/auth/withdraw")
    public ResponseEntity<String> withdraw(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok().body(authService.withdraw(userDetails.getUser().getId()));
    }
}