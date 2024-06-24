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

@Service
@RequiredArgsConstructor
public class ScrapService {

    private final ScrapRepository scrapRepository;
    private final PostRepository postRepository;


    public boolean scrapEitherOne(User user, Long postsId) {
        Post post = postRepository.findById(postsId).orElseThrow(
            () -> new EntityNotFoundException(NotFoundEntity.POST_NOT_FOUND)
        );
        Optional<Scrap> scrap = scrapRepository.findByUserAndPost(user, post);

        if (scrap.isEmpty()) {
            Scrap saveScrap = new Scrap(user, post);
            scrapRepository.save(saveScrap);
            return true;
        } else {
            scrapRepository.delete(scrap.get());
            return false;
        }
    }

    public List<ScrapResponseDto> getUserScraps(Long userId, User user) {
        user.validateId(userId);
        List<Scrap> scraps = scrapRepository.findAllByUser(user);
        return scraps.stream().map(scrap -> new ScrapResponseDto(scrap.getPost())).toList();
    }
}
