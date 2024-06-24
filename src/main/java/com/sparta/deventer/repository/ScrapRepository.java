package com.sparta.deventer.repository;

import com.sparta.deventer.entity.Post;
import com.sparta.deventer.entity.Scrap;
import com.sparta.deventer.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScrapRepository extends JpaRepository<Scrap, Long> {

    Optional<Scrap> findByUserAndPost(User user, Post post);

}
