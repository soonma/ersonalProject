package com.sparta.deventer.controller;

import com.sparta.deventer.security.UserDetailsImpl;
import com.sparta.deventer.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @PutMapping("/likes")
    public ResponseEntity<String> isLike(@AuthenticationPrincipal UserDetailsImpl userDetails
            , @RequestParam("contentType") String contentType
            , @RequestParam("contentId") Long contentId) {
        Boolean isLiked = likeService.likeComparison(contentType, contentId,
                userDetails.getUser().getId());
        String message = isLiked ? "좋아요가 완료 되었습니다." : "좋아요가 취소 되었습니다.";
        return ResponseEntity.ok()
                .body(message + "현재 좋아요 갯수 : " + likeService.likeCount(contentType, contentId));
    }
}
