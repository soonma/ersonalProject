package com.sparta.deventer.controller;

import com.sparta.deventer.dto.ChangeNicknameRequestDto;
import com.sparta.deventer.dto.UserResponseDto;
import com.sparta.deventer.enums.UserRole;
import com.sparta.deventer.service.AdminUserService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    /**
     * 사용자 전체 목록을 조회합니다.
     *
     * @return 전체 사용자 목록 응답 DTO
     */
    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        List<UserResponseDto> users = adminUserService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    /**
     * 사용자에게 관리자 권한을 부여합니다.
     *
     * @param userId 권한을 부여할 사용자 ID
     * @return 업데이트된 사용자 정보 응답 DTO
     */
    @PutMapping("/{userId}/role")
    public ResponseEntity<UserResponseDto> updateUserRole(@PathVariable Long userId) {

        UserResponseDto userResponseDto = adminUserService.updateUserRole(userId, UserRole.ADMIN);
        return ResponseEntity.ok(userResponseDto);
    }

    /**
     * 사용자의 닉네임을 수정합니다.
     *
     * @param userId                   닉네임을 수정할 사용자 ID
     * @param changeNicknameRequestDto 닉네임 변경 요청 DTO
     * @return 업데이트된 사용자 정보 응답 DTO
     */
    @PutMapping("/{userId}/nickname")
    public ResponseEntity<UserResponseDto> updateUserNickname(
        @PathVariable Long userId,
        @Valid @RequestBody ChangeNicknameRequestDto changeNicknameRequestDto) {

        UserResponseDto userResponseDto = adminUserService.updateUserNickname(userId,
            changeNicknameRequestDto);
        return ResponseEntity.ok(userResponseDto);
    }

    /**
     * 사용자를 차단합니다.
     *
     * @param userId 차단할 사용자 ID
     * @return 업데이트된 사용자 정보 응답 DTO
     */
    @PutMapping("/{userId}/block")
    public ResponseEntity<UserResponseDto> blockUser(@PathVariable Long userId) {

        UserResponseDto userResponseDto = adminUserService.blockUser(userId);
        return ResponseEntity.ok(userResponseDto);
    }

    /**
     * 사용자를 활성화합니다.
     *
     * @param userId 활성화할 사용자 ID
     * @return 업데이트된 사용자 정보 응답 DTO
     */
    @PutMapping("/{userId}/activate")
    public ResponseEntity<UserResponseDto> activateUser(@PathVariable Long userId) {

        UserResponseDto userResponseDto = adminUserService.activateUser(userId);
        return ResponseEntity.ok(userResponseDto);
    }

    /**
     * 사용자를 삭제합니다.
     *
     * @param userId 삭제할 사용자 ID
     * @return 업데이트된 사용자 정보 응답 DTO
     */
    @PutMapping("/{userId}/delete")
    public ResponseEntity<UserResponseDto> deleteUser(@PathVariable Long userId) {

        UserResponseDto userResponseDto = adminUserService.deleteUser(userId);
        return ResponseEntity.ok(userResponseDto);
    }
}