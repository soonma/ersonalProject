package impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.deventer.custom.LikeRepositoryCustom;
import com.sparta.deventer.entity.Like;
import com.sparta.deventer.entity.LikeableEntityType;
import com.sparta.deventer.entity.QLike;
import com.sparta.deventer.entity.QUser;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LikeRepositoryImpl implements LikeRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<Like> findByLikeableEntityIdAndLikeableEntityTypeAndUserId(
            Long likeableEntityId, LikeableEntityType likeableEntityType, Long userId) {
        QLike qLike = QLike.like;
        QUser qUser = QUser.user;

        return Optional.ofNullable(
                jpaQueryFactory
                        .select(qLike)
                        .where(qLike.likeableEntityId.eq(likeableEntityId))
                        .where(qLike.likeableEntityType.eq(likeableEntityType))
                        .join(qLike.user, qUser).fetchOne()
        );
    }

    @Override
    public List<Like> findAllByLikeableEntityIdAndLikeableEntityType(Long likeableEntityId,
            LikeableEntityType likeableEntityType) {
        QLike qLike = QLike.like;
        return jpaQueryFactory.select(qLike)
                .where(qLike.likeableEntityId.eq(likeableEntityId))
                .where(qLike.likeableEntityType.eq(likeableEntityType))
                .fetch();
    }

    @Override
    public List<Like> findAllByLikeableEntityTypeAndUserId(
            LikeableEntityType likeableEntityType, Long userId) {
        QLike qLike = QLike.like;
        return jpaQueryFactory.select(qLike)
                .where(qLike.likeableEntityType.eq(likeableEntityType))
                .where(qLike.user.id.eq(userId))
                .fetch();
    }
}
