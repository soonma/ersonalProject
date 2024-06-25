package com.sparta.deventer.controller;

import com.sparta.deventer.dto.ScrapResponseDto;
import com.sparta.deventer.security.UserDetailsImpl;
import com.sparta.deventer.service.ScrapService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/scraps")
public class ScrapController {

    private final ScrapService scrapService;

    /**
     * 게시물을 스크랩하거나 스크랩을 취소합니다.
     *
     * @param postId      스크랩할 게시물 ID
     * @param userDetails 현재 인증된 사용자 정보
     * @return 스크랩 처리 결과 메시지
     */
    @PostMapping("/posts/{postId}")
    public ResponseEntity<String> toggleScrap(
        @PathVariable Long postId,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {

        boolean isScrapped = scrapService.toggleScrap(postId, userDetails.getUser());
        return ResponseEntity.ok().body(isScrapped ? "게시물을 스크랩했습니다." : "게시물의 스크랩을 취소했습니다.");
    }

    /**
     * 사용자의 스크랩 목록을 조회합니다.
     *
     * @param userDetails 현재 인증된 사용자 정보
     * @return 스크랩 목록
     */
    @GetMapping("/users/{userId}")
    public ResponseEntity<List<ScrapResponseDto>> getUserScraps(
        @PathVariable Long userId,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {

        List<ScrapResponseDto> scrapResponseDtos = scrapService.getUserScraps(userId,
            userDetails.getUser());
        return ResponseEntity.ok(scrapResponseDtos);
    }
}