package com.sparta.deventer.repository;

import com.sparta.deventer.custom.ScrapRepositoryCustom;
import com.sparta.deventer.entity.Scrap;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScrapRepository extends JpaRepository<Scrap, Long>, ScrapRepositoryCustom {

//    Optional<Scrap> findByUserAndPost(User user, Post post);
//
//    List<Scrap> findAllByUser(User user);
}
