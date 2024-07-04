package com.sparta.deventer.custom;

import com.sparta.deventer.entity.Follow;
import com.sparta.deventer.entity.User;
import java.util.List;
import java.util.Optional;

public interface FollowRepositoryCustom {

    Optional<Follow> findByFollowingAndFollower(User followingUser, User followerUser);

    List<Follow> findByFollower(User followerUser);
}
