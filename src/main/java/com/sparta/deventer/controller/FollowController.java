package com.sparta.deventer.controller;

import com.sparta.deventer.dto.FollowWithPostResponseDto;
import com.sparta.deventer.security.UserDetailsImpl;
import com.sparta.deventer.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/follow")
@RequiredArgsConstructor
@RestController
public class FollowController {

    private static final int PAGE_SIZE = 5;
    private final FollowService followService;

    @PostMapping
    public ResponseEntity<String> toggleFollow(
            @RequestParam("following") Long following,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        boolean isFollow = followService.toggleFollow(following,
                userDetails.getUser());

        String message =
                userDetails.getUser().getNickname() + "님이 " + following + "의 " +
                        (isFollow ? "팔로우가 완료되었습니다.\n" : "팔로우가 취소되었습니다.\n");
        return ResponseEntity.ok()
                .body(message + "내가 팔로우한 수: " + followService.followCount(userDetails.getUser()));
    }

    @GetMapping("/post")
    public ResponseEntity<Page<FollowWithPostResponseDto>> followPostList(
            @AuthenticationPrincipal UserDetailsImpl userDetails

            , @RequestParam(defaultValue = "0") int page
    ) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);

        Page<FollowWithPostResponseDto> followPostPage = followService
                .followPostList(userDetails, pageable);

        return ResponseEntity.ok(followPostPage);
    }
}
