package impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.deventer.custom.PasswordHistoryRepositoryCustom;
import com.sparta.deventer.entity.PasswordHistory;
import com.sparta.deventer.entity.QPasswordHistory;
import com.sparta.deventer.entity.User;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PasswordHistoryRepositoryImpl implements PasswordHistoryRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<PasswordHistory> findByUserOrderByCreatedAtAsc(User user) {
        QPasswordHistory qPasswordHistory = QPasswordHistory.passwordHistory;

        return jpaQueryFactory.select(qPasswordHistory)
                .where(qPasswordHistory.user.eq(user))
                .fetch();
    }
}
