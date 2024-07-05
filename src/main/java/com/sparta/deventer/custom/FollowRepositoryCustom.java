package com.sparta.deventer.custom;

import com.sparta.deventer.dto.FollowTopTenResponseDto;
import com.sparta.deventer.entity.Follow;
import com.sparta.deventer.entity.User;
import java.util.List;
import java.util.Optional;

public interface FollowRepositoryCustom {

    Optional<Follow> findByFollowingAndFollower(User followingUser, User followerUser);

    List<Follow> findByFollower(User followerUser);

    //    @Query(
//            "select count(),u.nickname\n"
//                    + "from Follow f join User u\n"
//                    + "where f.following = u\n"
//                    + "group by u.nickname\n"
//                    + "order by 1 desc "
//
//    )
    List<FollowTopTenResponseDto> findAllGroupBy();
}
