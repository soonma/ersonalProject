package impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.deventer.custom.ScrapRepositoryCustom;
import com.sparta.deventer.entity.Post;
import com.sparta.deventer.entity.QScrap;
import com.sparta.deventer.entity.Scrap;
import com.sparta.deventer.entity.User;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ScrapRepositoryImpl implements ScrapRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;


    @Override
    public Optional<Scrap> findByUserAndPost(User user, Post post) {
        QScrap qScrap = QScrap.scrap;

        return Optional.ofNullable(
                jpaQueryFactory.select(qScrap)
                        .where(qScrap.user.eq(user))
                        .where(qScrap.post.eq(post))
                        .fetchOne()
        );
    }

    @Override
    public List<Scrap> findAllByUser(User user) {
        QScrap qScrap = QScrap.scrap;
        return jpaQueryFactory.select(qScrap)
                .where(qScrap.user.eq(user)).fetch();
    }
}
