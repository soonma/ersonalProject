package com.sparta.deventer.controller;

import com.sparta.deventer.dto.ChangePasswordRequestDto;
import com.sparta.deventer.dto.CommentResponseDto;
import com.sparta.deventer.dto.PostResponseDto;
import com.sparta.deventer.dto.ProfileResponseDto;
import com.sparta.deventer.dto.UpdateProfileRequestDto;
import com.sparta.deventer.entity.User;
import com.sparta.deventer.security.UserDetailsImpl;
import com.sparta.deventer.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}")
public class UserController {

    private static final int PAGE_SIZE = 10;
    private final UserService userService;

    /**
     * 사용자의 프로필을 조회합니다.
     *
     * @param userId      조회할 사용자 ID
     * @param userDetails 현재 인증된 사용자 정보
     * @return 프로필 응답 DTO
     */
    @GetMapping
    public ResponseEntity<ProfileResponseDto> getProfile(
        @PathVariable Long userId,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {

        User user = userDetails.getUser();
        ProfileResponseDto profileResponseDto = userService.getProfile(userId, user);
        return ResponseEntity.ok(profileResponseDto);
    }

    /**
     * 사용자의 모든 게시물을 조회합니다.
     *
     * @param userId      조회할 사용자 ID
     * @param userDetails 현재 인증된 사용자 정보
     * @param page        페이지 번호
     * @return 페이지 단위로 나눠진 게시물 응답 DTO
     */
    @GetMapping("/posts")
    public ResponseEntity<Page<PostResponseDto>> getAllPosts(
        @PathVariable Long userId,
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @RequestParam(defaultValue = "0") int page) {

        User user = userDetails.getUser();
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        Page<PostResponseDto> postResponseDtoPage = userService.getAllPosts(userId, user, pageable);
        return ResponseEntity.ok(postResponseDtoPage);
    }

    /**
     * 사용자의 모든 댓글을 조회합니다.
     *
     * @param userId      조회할 사용자 ID
     * @param userDetails 현재 인증된 사용자 정보
     * @param page        페이지 번호
     * @return 페이지 단위로 나눠진 댓글 응답 DTO
     */
    @GetMapping("/comments")
    public ResponseEntity<Page<CommentResponseDto>> getAllComments(
        @PathVariable Long userId,
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @RequestParam(defaultValue = "0") int page) {

        User user = userDetails.getUser();
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        Page<CommentResponseDto> commentResponseDtoPage = userService.getAllComments(userId, user,
            pageable);
        return ResponseEntity.ok(commentResponseDtoPage);
    }

    /**
     * 사용자의 프로필을 수정합니다.
     *
     * @param userId                  수정할 사용자 ID
     * @param updateProfileRequestDto 프로필 수정 요청 DTO
     * @param userDetails             현재 인증된 사용자 정보
     * @return 수정된 프로필 응답 DTO
     */
    @PutMapping
    public ResponseEntity<ProfileResponseDto> updateProfile(
        @PathVariable Long userId,
        @Valid @RequestBody UpdateProfileRequestDto updateProfileRequestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {

        User user = userDetails.getUser();
        ProfileResponseDto profileResponseDto = userService.updateProfile(userId,
            updateProfileRequestDto, user);
        return ResponseEntity.ok(profileResponseDto);
    }

    /**
     * 사용자의 비밀번호를 변경합니다.
     *
     * @param userId                   변경할 사용자 ID
     * @param changePasswordRequestDto 비밀번호 변경 요청 DTO
     * @param userDetails              현재 인증된 사용자 정보
     * @return 비밀번호 변경 완료 메시지
     */
    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword(
        @PathVariable Long userId,
        @Valid @RequestBody ChangePasswordRequestDto changePasswordRequestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {

        User user = userDetails.getUser();
        userService.changePassword(userId, changePasswordRequestDto, user);
        return ResponseEntity.ok().body("비밀번호를 변경했습니다.");
    }
}