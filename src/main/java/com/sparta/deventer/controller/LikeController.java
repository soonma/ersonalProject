package com.sparta.deventer.controller;

import com.sparta.deventer.dto.CommentResponseDto;
import com.sparta.deventer.dto.PostResponseDto;
import com.sparta.deventer.security.UserDetailsImpl;
import com.sparta.deventer.service.LikeService;
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

@RestController
@RequiredArgsConstructor
@RequestMapping(("/likes"))
public class LikeController {

    private static final int PAGE_SIZE = 5;
    private final LikeService likeService;

    /**
     * 좋아요를 처리합니다.
     *
     * @param userDetails        현재 인증된 사용자 정보
     * @param likeableEntityType 좋아요를 할 엔티티 유형
     * @param likeableEntityId   좋아요를 할 엔티티 ID
     * @return 좋아요 처리 결과 메시지
     */
    @PostMapping
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

    @GetMapping("/posts")
    public ResponseEntity<Page<PostResponseDto>> myLikePost(
            @RequestParam("likeableEntityType") String likeableEntityType,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam(defaultValue = "0") int page) {

        Pageable pageable = PageRequest.of(page, PAGE_SIZE);

        Page<PostResponseDto> postPage = likeService.myLikePost(
                likeableEntityType, userDetails, pageable);

        return ResponseEntity.ok().body(postPage);
    }

    @GetMapping(("/comments"))
    ResponseEntity<Page<CommentResponseDto>> myLikeComment(
            @RequestParam("likeableEntityType") String likeableEntityType,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam(defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);

        Page<CommentResponseDto> commentPage = likeService.myLikeComment(
                likeableEntityType, userDetails, pageable);

        return ResponseEntity.ok().body(commentPage);
    }


}