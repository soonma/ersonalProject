package impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.deventer.custom.CategoryRepositoryCustom;
import com.sparta.deventer.entity.Category;
import com.sparta.deventer.entity.QCategory;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CategoryRepositoryImpl implements CategoryRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Category> findByTopic(String categoryTopic) {
        return Optional.ofNullable(findQuery(categoryTopic));
    }

    @Override
    public boolean existsByTopic(String categoryTopic) {
        return findQuery(categoryTopic) != null;
    }

    public Category findQuery(String categoryTopic) {
        QCategory qCategory = QCategory.category;
        return queryFactory
                .select(qCategory)
                .where(qCategory.topic.eq(categoryTopic))
                .fetchOne();
    }
}
