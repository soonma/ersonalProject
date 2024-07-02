package impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.deventer.custom.UserRepositoryCustom;
import com.sparta.deventer.entity.QUser;
import com.sparta.deventer.entity.User;
import com.sparta.deventer.enums.UserLoginType;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;


    @Override
    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(findQuery(username));
    }

    @Override
    public boolean existsByUsername(String username) {
        return findQuery(username) != null;
    }

    @Override
    public boolean existsByNickname(String nickname) {
        return findQuery(nickname) != null;
    }

    @Override
    public boolean existsByEmail(String email) {
        return findQuery(email) != null;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return Optional.ofNullable(findQuery(email));
    }

    @Override
    public Optional<User> findByEmailAndLoginType(String email, UserLoginType loginType) {
        QUser qUser = QUser.user;
        User findUser = jpaQueryFactory
                .select(qUser)
                .where(qUser.email.eq(email))
                .where(qUser.loginType.eq(loginType))
                .fetchOne();

        return Optional.ofNullable(findUser);
    }


    public User findQuery(String where) {
        QUser qUser = QUser.user;
        return jpaQueryFactory
                .select(qUser)
                .where(qUser.username.eq(where))
                .fetchOne();
    }
}
