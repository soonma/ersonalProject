package impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.deventer.custom.FollowRepositoryCustom;
import com.sparta.deventer.entity.Follow;
import com.sparta.deventer.entity.QFollow;
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

        return Optional.ofNullable(queryFactory.select(qFollow)
                .where(qFollow.following.eq(followingUser))
                .where(qFollow.follower.eq(followerUser))
                .fetchOne());
    }

    @Override
    public List<Follow> findByFollower(User followerUser) {
        QFollow qFollow = QFollow.follow;
        return queryFactory.select(qFollow)
                .where(qFollow.follower.eq(followerUser))
                .fetch();
    }


}
