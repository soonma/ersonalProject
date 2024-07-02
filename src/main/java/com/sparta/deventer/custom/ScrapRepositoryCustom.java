package com.sparta.deventer.custom;

import com.sparta.deventer.entity.Post;
import com.sparta.deventer.entity.Scrap;
import com.sparta.deventer.entity.User;
import java.util.List;
import java.util.Optional;

public interface ScrapRepositoryCustom {

    Optional<Scrap> findByUserAndPost(User user, Post post);

    List<Scrap> findAllByUser(User user);
}
