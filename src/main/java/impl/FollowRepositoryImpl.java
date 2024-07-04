package impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.deventer.custom.FollowRepositoryCustom;
import com.sparta.deventer.entity.Follow;
import com.sparta.deventer.entity.QFollow;
import com.sparta.deventer.entity.QUser;
import com.sparta.deventer.entity.User;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor

public class FollowRepositoryImpl implements FollowRepositoryCustom {

    private final JPAQueryFactory queryFactory;


    @Override
    public Optional<Follow> findByFollowingAndFollower(User followingUser, User followerUser) {
        QFollow qFollow = QFollow.follow;
        QUser qFollowingUser = QUser.user;
        QUser qFollowerUser = QUser.user;

        return Optional.ofNullable(queryFactory.select(qFollow)
                .join(qFollow.following, qFollowingUser)
                .join(qFollow.follower, qFollowerUser)
                .where(qFollowingUser.eq(followingUser))
                .where(qFollowerUser.eq(followerUser))
                .fetchOne());
    }

    @Override
    public List<Follow> findByFollower(User followerUser) {
        QFollow qFollow = QFollow.follow;
        QUser qFollowingUser = QUser.user;
        QUser qFollowerUser = QUser.user;

        return queryFactory.select(qFollow)
                .join(qFollow.following, qFollowingUser)
                .join(qFollow.follower, qFollowerUser)
                .where(qFollow.follower.eq(followerUser))
                .orderBy(qFollow.following.nickname.asc())
                .fetch();
    }
}
