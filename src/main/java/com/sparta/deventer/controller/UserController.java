package com.sparta.deventer.controller;

import com.sparta.deventer.dto.ChangePasswordRequestDto;
import com.sparta.deventer.dto.ProfileResponseDto;
import com.sparta.deventer.dto.UpdateProfileRequestDto;
import com.sparta.deventer.entity.User;
import com.sparta.deventer.exception.UserNotFoundException;
import com.sparta.deventer.repository.UserRepository;
import com.sparta.deventer.security.UserDetailsImpl;
import com.sparta.deventer.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getProfile(@PathVariable Long userId,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {

        User user = getUserFromUserDetails(userDetails);
        ProfileResponseDto profileResponseDto = userService.getProfile(userId, user);
        return ResponseEntity.ok(profileResponseDto);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Object> updateProfile(@PathVariable Long userId,
        @RequestBody UpdateProfileRequestDto updateProfileRequestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {

        User user = getUserFromUserDetails(userDetails);
        ProfileResponseDto profileResponseDto = userService.updateProfile(userId,
            updateProfileRequestDto, user);
        return ResponseEntity.ok(profileResponseDto);
    }

    @PutMapping("/{userId}/change-password")
    public ResponseEntity<Object> changePassword(@PathVariable Long userId,
        @RequestBody ChangePasswordRequestDto changePasswordRequestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {

        User user = getUserFromUserDetails(userDetails);
        userService.changePassword(userId, changePasswordRequestDto, user);
        return ResponseEntity.noContent().build();
    }

    private User getUserFromUserDetails(UserDetailsImpl userDetails) {
        return userRepository.findById(userDetails.getUser().getId())
            .orElseThrow(() -> new UserNotFoundException("해당 사용자를 찾을 수 없습니다."));
    }
}