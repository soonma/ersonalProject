package com.sparta.deventer.controller;

import com.sparta.deventer.security.UserDetailsImpl;
import com.sparta.deventer.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    /**
     * 좋아요를 처리합니다.
     *
     * @param userDetails        현재 인증된 사용자 정보
     * @param likeableEntityType 좋아요를 할 엔티티 유형
     * @param likeableEntityId   좋아요를 할 엔티티 ID
     * @return 좋아요 처리 결과 메시지
     */
    @PostMapping("/likes")
    public ResponseEntity<String> toggleLike(
        @RequestParam("likeableEntityId") Long likeableEntityId,
        @RequestParam("likeableEntityType") String likeableEntityType,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {

        boolean isLiked = likeService.toggleLike(likeableEntityId, likeableEntityType,
            userDetails.getUser());

        // e.g. "post(Id: 1)의 좋아요가 완료되었습니다."
        String message =
            likeableEntityType + "(Id: " + likeableEntityId + ")의 " +
                (isLiked ? "좋아요가 완료되었습니다.\n" : "좋아요가 취소되었습니다.\n");
        return ResponseEntity.ok()
            .body(message + "좋아요 개수: " + likeService.likeCount(likeableEntityId,
                likeableEntityType));
    }
}