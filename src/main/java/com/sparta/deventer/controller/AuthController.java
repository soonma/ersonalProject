package com.sparta.deventer.controller;

import com.sparta.deventer.dto.AdminSignUpRequestDto;
import com.sparta.deventer.dto.LoginRequestDto;
import com.sparta.deventer.dto.UserSignUpRequestDto;
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

    @PostMapping("/auth/sign-up")
    public ResponseEntity<String> userSignUp(@Valid @RequestBody UserSignUpRequestDto requestDto) {
        return ResponseEntity.ok().body(authService.userSignUp(requestDto));
    }

    @PostMapping("/auth/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequestDto requestDto,
            HttpServletResponse response) {
        return ResponseEntity.ok().body(authService.login(requestDto, response));
    }

    @PostMapping("/auth/refresh")
    public ResponseEntity<String> tokenReissue(HttpServletRequest request,
            HttpServletResponse response) {
        return ResponseEntity.ok().body(authService.tokenReissue(request, response));
    }

    @PostMapping("/auth/logout")
    public ResponseEntity<String> logout(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok().body(authService.logout(userDetails.getUser().getId()));
    }

    @DeleteMapping("/auth/withdraw")
    public ResponseEntity<String> withdraw(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok().body(authService.withdraw(userDetails.getUser().getId()));
    }

    @PostMapping("/auth/sign-up/admin")
    public ResponseEntity<String> AdminSignUp(
            @Valid @RequestBody AdminSignUpRequestDto requestDto) {
        return ResponseEntity.ok().body(authService.adminSignUp(requestDto));
    }
}