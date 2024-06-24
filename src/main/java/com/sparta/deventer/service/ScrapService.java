package com.sparta.deventer.service;

import com.sparta.deventer.entity.Post;
import com.sparta.deventer.entity.Scrap;
import com.sparta.deventer.entity.User;
import com.sparta.deventer.exception.PostNotFoundException;
import com.sparta.deventer.repository.PostRepository;
import com.sparta.deventer.repository.ScrapRepository;
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
                () -> new PostNotFoundException("게시글이 존재 하지 않습니다.")
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
}
