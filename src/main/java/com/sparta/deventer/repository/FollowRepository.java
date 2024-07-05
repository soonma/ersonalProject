package com.sparta.deventer.repository;

import com.sparta.deventer.custom.FollowRepositoryCustom;
import com.sparta.deventer.entity.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow, Long>, FollowRepositoryCustom {

    //
//    Optional<Follow> findByFollowingAndFollower(User followingUser, User followerUser);
//
//    List<Follow> findByFollower(User followerUser);
    
}
