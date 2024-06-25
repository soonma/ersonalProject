package com.sparta.deventer.service;

import com.sparta.deventer.dto.ScrapResponseDto;
import com.sparta.deventer.entity.Post;
import com.sparta.deventer.entity.Scrap;
import com.sparta.deventer.entity.User;
import com.sparta.deventer.enums.NotFoundEntity;
import com.sparta.deventer.exception.EntityNotFoundException;
import com.sparta.deventer.repository.PostRepository;
import com.sparta.deventer.repository.ScrapRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ScrapService {

    private final ScrapRepository scrapRepository;
    private final PostRepository postRepository;

    /**
     * 게시물 스크랩을 토글합니다.
     *
     * @param postId 스크랩할 게시물의 ID
     * @param user   현재 인증된 사용자
     * @return 스크랩이 완료되었는지 여부
     */
    @Transactional
    public boolean toggleScrap(Long postId, User user) {
        Post post = getPostByIdOrThrow(postId);

        Optional<Scrap> optionalScrap = scrapRepository.findByUserAndPost(user, post);
        if (optionalScrap.isEmpty()) {
            Scrap scrap = new Scrap(user, post);
            scrapRepository.save(scrap);
            return true;
        } else {
            scrapRepository.delete(optionalScrap.get());
            return false;
        }
    }

    /**
     * 사용자의 모든 스크랩을 조회합니다.
     *
     * @param userId 조회할 사용자의 ID
     * @param user   현재 인증된 사용자
     * @return 사용자의 스크랩 목록
     */
    public List<ScrapResponseDto> getUserScraps(Long userId, User user) {
        validateUserId(userId, user);
        List<Scrap> scraps = scrapRepository.findAllByUser(user);
        return scraps.stream().map(scrap -> new ScrapResponseDto(scrap.getPost())).toList();
    }

    /**
     * 사용자의 ID가 유효한지 검증합니다.
     *
     * @param userId 검증할 사용자 ID
     * @param user   현재 인증된 사용자
     */
    private void validateUserId(Long userId, User user) {
        if (!user.getId().equals(userId)) {
            throw new EntityNotFoundException(NotFoundEntity.USER_NOT_FOUND);
        }
    }

    /**
     * ID를 기준으로 게시물을 조회하고, 없을 경우 예외를 던집니다.
     *
     * @param postId 조회할 게시물의 ID
     * @return 조회된 게시물
     */
    private Post getPostByIdOrThrow(Long postId) {
        return postRepository.findById(postId)
            .orElseThrow(() -> new EntityNotFoundException(NotFoundEntity.POST_NOT_FOUND));
    }
}