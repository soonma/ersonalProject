package com.sparta.deventer.controller;

import com.sparta.deventer.security.UserDetailsImpl;
import com.sparta.deventer.service.AdminUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/users")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminUserController {

    private final AdminUserService adminUserService;

    // (관리자) 사용자 전체 목록 조회
    @GetMapping
    public ResponseEntity<?> getAllUsers(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return null;
    }

    // (관리자) 사용자 권한 부여
    @PutMapping("/{userId}/role")
    public ResponseEntity<?> updateUserRole(@PathVariable Long userId,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return null;
    }

    // (관리자) 사용자 닉네임 수정
    @PutMapping("/{userId}/nickname")
    public ResponseEntity<?> updateUserNickname(@PathVariable Long userId,
        @RequestBody String newNickname,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return null;
    }

    // (관리자) 사용자 차단
    @PutMapping("/{userId}/status")
    public ResponseEntity<?> blockUser(@PathVariable Long userId,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return null;
    }

    // (관리자) 사용자 삭제
    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return null;
    }
}